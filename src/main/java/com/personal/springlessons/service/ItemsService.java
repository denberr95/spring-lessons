package com.personal.springlessons.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.ItemsEntity;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.repository.IOrderItemsRepository;
import com.personal.springlessons.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;
    private final Tracer tracer;
    private final IItemsMapper itemMapper;
    private final IOrderItemsRepository orderItemsRepository;
    private final IItemsRepository itemsRepository;

    @NewSpan
    public void upload(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.NUMBER_ITEMS_TO_UPLOAD, items.size());

        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(items.size());
        orderItemsEntity.setStatus(ItemStatus.UPLOAD);
        OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(orderItemsEntity);

        items.forEach(i -> {
            KafkaMessageItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.UPLOAD);
            message.setIdOrderItems(row.getId().toString());
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    @NewSpan
    public void delete(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.NUMBER_ITEMS_TO_DELETE, items.size());

        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(items.size());
        orderItemsEntity.setStatus(ItemStatus.DELETE);
        OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(orderItemsEntity);

        items.forEach(i -> {
            KafkaMessageItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.DELETE);
            message.setIdOrderItems(row.getId().toString());
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    @NewSpan
    public List<ItemDTO> getAll() {
        List<ItemsEntity> data = new ArrayList<>();
        List<OrderItemsEntity> orderItemsEntities = this.orderItemsRepository.findAll();
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.TOTAL_ORDERS, String.valueOf(orderItemsEntities.size()))
                .event("Orders retrieved");

        orderItemsEntities.forEach(x -> {
            currentSpan.tag(Constants.ID_ORDER_ITEMS, x.getId().toString());
            Optional<List<ItemsEntity>> rows = this.itemsRepository.findByItemsStatusEntity(x);
            if (!rows.isEmpty()) {
                data.addAll(rows.get());
            }
        });
        currentSpan.tag(Constants.TOTAL_ITEMS, data.size());
        return this.itemMapper.mapDTO(data);
    }

    private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
        Span span = this.tracer.nextSpan().name("notifyKafkaItems");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
            this.kafkaTemplate.send(topic, message);
            span.tag(Constants.OPERATION_TYPE, message.getItemStatus().name());
            span.tag(Constants.ID_ORDER_ITEMS, message.getIdOrderItems());
            span.tag(Constants.BARCODE, message.getBarcode());
            span.event("Kafka message sent");
        } finally {
            span.end();
        }
    }
}
