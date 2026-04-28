package com.personal.springlessons.component.kafka.filter;

import org.junit.jupiter.api.Test;

class UploadItemsRecordFilterTest {

  @Test
  void givenNullRecord_whenFilter_thenReturnFalse() {
    // TODO
  }

  @Test
  void givenRecordWithUnexpectedValueType_whenFilter_thenReturnTrue() {
    // TODO
  }

  @Test
  void givenDeleteStatusMessage_whenFilter_thenReturnTrue() {
    // TODO
  }

  @Test
  void givenUploadStatusMessage_whenFilter_thenReturnFalse() {
    // TODO
  }
}
