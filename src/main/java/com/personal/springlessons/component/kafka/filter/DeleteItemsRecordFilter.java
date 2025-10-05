package com.personal.springlessons.component.kafka.filter;

import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Component
public class DeleteItemsRecordFilter<K, V> implements RecordFilterStrategy<K, V> {

  private static final Logger log = LoggerFactory.getLogger(DeleteItemsRecordFilter.class);

  @Override
  public boolean filter(ConsumerRecord<K, V> consumerRecord) {
    boolean result = false;
    if (null != consumerRecord) {
      KafkaMessageItemDTO message = (KafkaMessageItemDTO) consumerRecord.value();
      if (!ItemStatus.DELETE.equals(message.getItemStatus())) {
        result = true;
        log.debug(
            "Filter for item status: 'DELETE', item status received: '{}' message filtered on"
                + " topic: '{}', offset: '{}', partition: '{}'",
            message.getItemStatus().name(), consumerRecord.topic(), consumerRecord.offset(),
            consumerRecord.partition());
      }
    }
    return result;
  }
}
