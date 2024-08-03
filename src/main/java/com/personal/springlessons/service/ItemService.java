package com.personal.springlessons.service;

import java.util.List;
import com.personal.springlessons.component.mapper.IItemMapper;
import com.personal.springlessons.model.dto.ItemDTO;
import com.personal.springlessons.model.dto.KafkaMessagetItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;
import com.personal.springlessons.util.Constants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final KafkaTemplate<String, KafkaMessagetItemDTO> kafkaTemplate;
    private final Tracer tracer;
    private final IItemMapper itemMapper;

    /**
     * TODO
     * 
     * Gestire tracciamento richieste items
     * Gestire tracing da rest a kafka
     */

    @NewSpan
    public void upload(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag("number.items.to.upload", items.size());
        items.forEach(i -> {
            KafkaMessagetItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.UPLOAD);
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    @NewSpan
    public void delete(List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag("number.items.to.delete", items.size());
        items.forEach(i -> {
            KafkaMessagetItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.DELETE);
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    @NewSpan
    public void update(String id, List<ItemDTO> items) {
        Span currentSpan = this.tracer.currentSpan();
        currentSpan.tag("number.items.to.update", items.size());
        items.forEach(i -> {
            KafkaMessagetItemDTO message = this.itemMapper.mapMessage(i);
            message.setItemStatus(ItemStatus.UPDATE);
            this.notifyKafkaItems(message, Constants.TOPIC_ITEMS);
        });
    }

    private void notifyKafkaItems(KafkaMessagetItemDTO message, String topic) {
        Span span = this.tracer.nextSpan().name("notifyKafkaItems");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(span.start())) {
            this.kafkaTemplate.send(topic, message);
            span.tag("operation.type", message.getItemStatus().name());
            span.tag("barcode", message.getBarcode());
        } finally {
            span.end();
        }
    }
}
