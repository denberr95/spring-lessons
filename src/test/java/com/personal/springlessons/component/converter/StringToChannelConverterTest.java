package com.personal.springlessons.component.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.personal.springlessons.model.lov.Channel;

import org.junit.jupiter.api.Test;

class StringToChannelConverterTest {

  private final StringToChannelConverter converter = new StringToChannelConverter();

  @Test
  void givenValidChannelString_whenConvert_thenReturnChannel() {
    Channel result = this.converter.convert("Postman");
    assertEquals(Channel.POSTMAN, result);
  }

  @Test
  void givenInvalidChannelString_whenConvert_thenThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> this.converter.convert("INVALID"));
  }

  @Test
  void givenNullString_whenConvert_thenThrowIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> this.converter.convert(null));
  }
}
