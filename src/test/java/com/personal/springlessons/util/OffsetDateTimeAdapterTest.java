package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class OffsetDateTimeAdapterTest {

  private final OffsetDateTimeAdapter adapter = new OffsetDateTimeAdapter();

  @Test
  void givenValidDateTimeString_whenUnmarshal_thenReturnOffsetDateTime() {
    OffsetDateTime result = this.adapter.unmarshal("2024-01-15T10:30:00+00:00");
    assertEquals(OffsetDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC), result);
  }

  @Test
  void givenNullString_whenUnmarshal_thenReturnNull() {
    assertNull(this.adapter.unmarshal(null));
  }

  @Test
  void givenOffsetDateTime_whenMarshal_thenReturnIsoOffsetDateTimeString() {
    OffsetDateTime dt = OffsetDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC);
    String result = this.adapter.marshal(dt);
    assertEquals("2024-01-15T10:30:00Z", result);
  }

  @Test
  void givenNullDateTime_whenMarshal_thenReturnNull() {
    assertNull(this.adapter.marshal(null));
  }
}
