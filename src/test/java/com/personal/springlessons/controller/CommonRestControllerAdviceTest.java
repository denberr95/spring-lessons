package com.personal.springlessons.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonRestControllerAdviceTest {

  @Test
  void givenInvalidUUIDException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenSpringLessonsApplicationException_whenHandleException_thenReturnInternalServerError() {
    // TODO
  }

  @Test
  void givenMethodArgumentNotValidException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenMissingRequestHeaderException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenMethodArgumentTypeMismatchException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenConstraintViolationException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenHttpMessageNotReadableException_whenHandleException_thenReturnBadRequest() {
    // TODO
  }

  @Test
  void givenConcurrentUpdateException_whenHandleException_thenReturnConflict() {
    // TODO
  }
}
