package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class LocalTimeAdapterTest {

  private final LocalTimeAdapter adapter = new LocalTimeAdapter();

  @Test
  void givenValidTimeString_whenUnmarshal_thenReturnLocalTime() {
    LocalTime result = this.adapter.unmarshal("10:30:00");
    assertEquals(LocalTime.of(10, 30, 0), result);
  }

  @Test
  void givenNullString_whenUnmarshal_thenReturnNull() {
    assertNull(this.adapter.unmarshal(null));
  }

  @Test
  void givenLocalTime_whenMarshal_thenReturnIsoTimeString() {
    String result = this.adapter.marshal(LocalTime.of(10, 30, 0));
    assertEquals("10:30:00", result);
  }

  @Test
  void givenNullTime_whenMarshal_thenReturnNull() {
    assertNull(this.adapter.marshal(null));
  }
}
