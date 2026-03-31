package com.personal.springlessons.component.kafka.filter;

import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Component
public class UploadItemsRecordFilter<K, V> implements RecordFilterStrategy<K, V> {

  private static final Logger log = LoggerFactory.getLogger(UploadItemsRecordFilter.class);

  @Override
  public boolean filter(ConsumerRecord<K, V> consumerRecord) {
    if (null == consumerRecord) {
      return false;
    }
    if (!(consumerRecord.value() instanceof KafkaMessageItemDTO message)) {
      log.warn("Unexpected message type on topic '{}': {}", consumerRecord.topic(),
          consumerRecord.value() == null ? "null" : consumerRecord.value().getClass().getName());
      return true;
    }
    if (!ItemStatus.UPLOAD.equals(message.getItemStatus())) {
      log.debug(
          "Filter for item status: 'UPLOAD', item status received: '{}' message filtered on"
              + " topic: '{}', offset: '{}', partition: '{}'",
          message.getItemStatus().name(), consumerRecord.topic(), consumerRecord.offset(),
          consumerRecord.partition());
      return true;
    }
    return false;
  }
}
