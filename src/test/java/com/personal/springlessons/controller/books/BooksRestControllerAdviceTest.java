package com.personal.springlessons.controller.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BooksRestControllerAdviceTest {

  @Test
  void givenBookNotFoundException_whenHandleException_thenReturnNotFound() {
    // TODO
  }

  @Test
  void givenDuplicatedBookException_whenHandleException_thenReturnConflict() {
    // TODO
  }

  @Test
  void givenInvalidFileTypeException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenCSVContentValidationException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }
}
