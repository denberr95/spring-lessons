package com.personal.springlessons.component.kafka.filter;

import com.personal.springlessons.model.dto.KafkaMessageItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "deleteItemsFilter")
public class DeleteItemsRecordFilter<K, V> implements RecordFilterStrategy<K, V> {

    @Override
    public boolean filter(ConsumerRecord<K, V> consumerRecord) {
        boolean result = false;
        if (null != consumerRecord) {
            KafkaMessageItemDTO message = (KafkaMessageItemDTO) consumerRecord.value();
            if (!ItemStatus.DELETE.equals(message.getItemStatus())) {
                result = true;
                log.debug(
                        "Filter for item status: 'DELETE', item status received: '{}' message filtered on topic: '{}', offset: '{}', partition: '{}'",
                        message.getItemStatus().name(), consumerRecord.topic(),
                        consumerRecord.offset(), consumerRecord.partition());
            }
        }
        return result;
    }
}
