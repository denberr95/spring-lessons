package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class LocalDateAdapterTest {

  private final LocalDateAdapter adapter = new LocalDateAdapter();

  @Test
  void givenValidDateString_whenUnmarshal_thenReturnLocalDate() {
    LocalDate result = this.adapter.unmarshal("2024-01-15");
    assertEquals(LocalDate.of(2024, 1, 15), result);
  }

  @Test
  void givenNullString_whenUnmarshal_thenReturnNull() {
    assertNull(this.adapter.unmarshal(null));
  }

  @Test
  void givenLocalDate_whenMarshal_thenReturnIsoDateString() {
    String result = this.adapter.marshal(LocalDate.of(2024, 1, 15));
    assertEquals("2024-01-15", result);
  }

  @Test
  void givenNullDate_whenMarshal_thenReturnNull() {
    assertNull(this.adapter.marshal(null));
  }
}
