package com.personal.springlessons.component.kafka.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.personal.springlessons.model.dto.response.KafkaMessageItemDTO;
import com.personal.springlessons.model.lov.ItemStatus;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

class DeleteItemsRecordFilterTest {

  private final DeleteItemsRecordFilter<String, Object> filter = new DeleteItemsRecordFilter<>();

  @Test
  void givenNullRecord_whenFilter_thenReturnFalse() {
    assertFalse(this.filter.filter(null));
  }

  @Test
  void givenRecordWithUnexpectedValueType_whenFilter_thenReturnTrue() {
    ConsumerRecord<String, Object> record =
        new ConsumerRecord<>("topic-items", 0, 0L, "key", "not-a-dto");
    assertTrue(this.filter.filter(record));
  }

  @Test
  void givenUploadStatusMessage_whenFilter_thenReturnTrue() {
    KafkaMessageItemDTO message = new KafkaMessageItemDTO();
    message.setItemStatus(ItemStatus.UPLOAD);
    ConsumerRecord<String, Object> record =
        new ConsumerRecord<>("topic-items", 0, 0L, "key", message);
    assertTrue(this.filter.filter(record));
  }

  @Test
  void givenDeleteStatusMessage_whenFilter_thenReturnFalse() {
    KafkaMessageItemDTO message = new KafkaMessageItemDTO();
    message.setItemStatus(ItemStatus.DELETE);
    ConsumerRecord<String, Object> record =
        new ConsumerRecord<>("topic-items", 0, 0L, "key", message);
    assertFalse(this.filter.filter(record));
  }
}
