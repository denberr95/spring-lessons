package com.personal.springlessons.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.items.ItemsEntity;
import com.personal.springlessons.model.entity.orderitems.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.repository.IItemsRepository;
import com.personal.springlessons.repository.IOrderItemsRepository;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final Tracer tracer;
    private final IItemsMapper itemMapper;
    private final IItemsRepository itemsRepository;
    private final IOrderItemsRepository orderItemsRepository;
    private final KafkaTemplate<String, KafkaMessageItemDTO> kafkaTemplate;

    @NewSpan
    public void upload(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag(Constants.SPAN_KEY_NUMBER_ITEMS_TO_UPLOAD, items.size());

        OrderItemsEntity data = new OrderItemsEntity();
        data.setQuantity(items.size());
        data.setStatus(ItemStatus.UPLOAD);
        OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(data);

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
        currentSpan.tag(Constants.SPAN_KEY_NUMBER_ITEMS_TO_DELETE, items.size());

        OrderItemsEntity data = new OrderItemsEntity();
        data.setQuantity(items.size());
        data.setStatus(ItemStatus.DELETE);
        OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(data);

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
        Span currentSpan = this.tracer.currentSpan();
        List<OrderItemsEntity> ordersData = this.orderItemsRepository.findAll();
        currentSpan.tag(Constants.SPAN_KEY_TOTAL_ORDERS, String.valueOf(ordersData.size()))
                .event("Orders retrieved");

        ordersData.forEach(i -> {
            currentSpan.tag(Constants.SPAN_KEY_ID_ORDER_ITEMS, i.getId().toString());
            Optional<List<ItemsEntity>> itemsData = this.itemsRepository.findByorderItemsEntity(i);
            if (!itemsData.get().isEmpty()) {
                data.addAll(itemsData.get());
                currentSpan.tag(Constants.SPAN_KEY_COLLECTED_ITEMS, itemsData.get().size())
                        .event("Items collected");
            }
        });
        currentSpan.tag(Constants.SPAN_KEY_TOTAL_ITEMS, data.size());
        return this.itemMapper.mapDTO(data);
    }

    private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
        Span span = this.tracer.nextSpan().name("notifyKafkaItems");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
            this.kafkaTemplate.send(Methods.createKafkaMessage(message, topic));
            span.tag(Constants.SPAN_KEY_OPERATION_TYPE, message.getItemStatus().name());
            span.tag(Constants.SPAN_KEY_ID_ORDER_ITEMS, message.getIdOrderItems());
            span.tag(Constants.SPAN_KEY_BARCODE, message.getBarcode());
            span.event("Kafka message sent");
        } finally {
            span.end();
        }
    }
}
