package com.personal.springlessons.component.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import com.personal.springlessons.model.lov.Genre;

import org.junit.jupiter.api.Test;

class MappingUtilsTest {

  private final MappingUtils mappingUtils = new MappingUtils();

  @Test
  void givenValidDateString_whenStringToLocalDate_thenReturnLocalDate() {
    LocalDate result = this.mappingUtils.stringToLocalDate("2024-01-15");
    assertEquals(LocalDate.of(2024, 1, 15), result);
  }

  @Test
  void givenNullString_whenStringToLocalDate_thenReturnNull() {
    assertNull(this.mappingUtils.stringToLocalDate(null));
  }

  @Test
  void givenBlankString_whenStringToLocalDate_thenReturnNull() {
    assertNull(this.mappingUtils.stringToLocalDate("   "));
  }

  @Test
  void givenInvalidDateString_whenStringToLocalDate_thenReturnNull() {
    assertNull(this.mappingUtils.stringToLocalDate("not-a-date"));
  }

  @Test
  void givenValidNumberString_whenStringToInteger_thenReturnInteger() {
    assertEquals(42, this.mappingUtils.stringToInteger("42"));
  }

  @Test
  void givenNullString_whenStringToInteger_thenReturnNull() {
    assertNull(this.mappingUtils.stringToInteger(null));
  }

  @Test
  void givenBlankString_whenStringToInteger_thenReturnNull() {
    assertNull(this.mappingUtils.stringToInteger("  "));
  }

  @Test
  void givenInvalidNumberString_whenStringToInteger_thenReturnNull() {
    assertNull(this.mappingUtils.stringToInteger("not-a-number"));
  }

  @Test
  void givenValidGenreString_whenStringToGenre_thenReturnGenre() {
    assertEquals(Genre.NOIR, this.mappingUtils.stringToGenre("NOIR"));
  }

  @Test
  void givenNullString_whenStringToGenre_thenReturnNull() {
    assertNull(this.mappingUtils.stringToGenre(null));
  }

  @Test
  void givenInvalidGenreString_whenStringToGenre_thenReturnNull() {
    assertNull(this.mappingUtils.stringToGenre("INVALID_GENRE"));
  }
}
