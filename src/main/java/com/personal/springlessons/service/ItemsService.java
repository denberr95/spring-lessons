package com.personal.springlessons.service;

import java.util.List;
import com.personal.springlessons.component.mapper.IItemsMapper;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.entity.OrderItemsEntity;
import com.personal.springlessons.model.lov.ItemStatus;
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

    @NewSpan
    public void upload(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag("number.items.to.upload", items.size());

        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(items.size());
        orderItemsEntity.setStatus(ItemStatus.UPLOAD);
        final OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(orderItemsEntity);

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
        currentSpan.tag("number.items.to.delete", items.size());

        OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
        orderItemsEntity.setQuantity(items.size());
        orderItemsEntity.setStatus(ItemStatus.DELETE);
        final OrderItemsEntity row = this.orderItemsRepository.saveAndFlush(orderItemsEntity);

        items.forEach(i -> {
            KafkaMessageItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.DELETE);
            message.setIdOrderItems(row.getId().toString());
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    private void notifyKafkaItems(KafkaMessageItemDTO message, String topic) {
        Span span = this.tracer.nextSpan().name("notifyKafkaItems");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
            this.kafkaTemplate.send(topic, message);
            span.tag("operation.type", message.getItemStatus().name());
            span.tag("id.order.items", message.getIdOrderItems());
            span.tag("barcode", message.getBarcode());
            span.event("Kafka message sent");
        } finally {
            span.end();
        }
    }
}
