package com.personal.springlessons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import jakarta.validation.ConstraintViolationException;

import com.personal.springlessons.exception.ConcurrentUpdateException;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.lov.Channel;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class CommonRestControllerAdviceTest {

  private final CommonRestControllerAdvice advice = new CommonRestControllerAdvice();

  private WebRequest createWebRequest() {
    return new ServletWebRequest(new MockHttpServletRequest("GET", "/spring-app/v1/books"));
  }

  @Test
  void givenInvalidUUIDException_whenHandleException_thenReturnBadRequest() {
    ResponseEntity<?> response = this.advice.handleInvalidUUIDException(
        new InvalidUUIDException("not-a-uuid"), this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenSpringLessonsApplicationException_whenHandleException_thenReturnInternalServerError() {
    ResponseEntity<?> response = this.advice.handleSpringLessonsApplicationException(
        new SpringLessonsApplicationException("error"), this.createWebRequest());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void givenMethodArgumentNotValidException_whenHandleException_thenReturnBadRequest()
      throws NoSuchMethodException {
    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(new Object(), "bookDTO");
    bindingResult.addError(new FieldError("bookDTO", "name", "must not be blank"));
    org.springframework.core.MethodParameter parameter =
        new org.springframework.core.MethodParameter(String.class.getMethod("length"), -1);
    MethodArgumentNotValidException ex =
        new MethodArgumentNotValidException(parameter, bindingResult);

    ResponseEntity<?> response =
        this.advice.handleMethodArgumentNotValidException(ex, this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenMissingRequestHeaderException_whenHandleException_thenReturnBadRequest()
      throws NoSuchMethodException {
    org.springframework.core.MethodParameter parameter =
        new org.springframework.core.MethodParameter(String.class.getMethod("length"), -1);
    MissingRequestHeaderException ex = new MissingRequestHeaderException("channel", parameter);

    ResponseEntity<?> response =
        this.advice.handleMissingRequestHeaderException(ex, this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenMethodArgumentTypeMismatchException_whenHandleException_thenReturnBadRequest()
      throws NoSuchMethodException {
    org.springframework.core.MethodParameter parameter =
        new org.springframework.core.MethodParameter(String.class.getMethod("length"), -1);
    MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException("INVALID",
        Channel.class, "channel", parameter, null);

    ResponseEntity<?> response =
        this.advice.handleMethodArgumentTypeMismatchException(ex, this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenConstraintViolationException_whenHandleException_thenReturnBadRequest() {
    ResponseEntity<?> response = this.advice.handleConstraintViolationException(
        new ConstraintViolationException(Set.of()), this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenHttpMessageNotReadableException_whenHandleException_thenReturnBadRequest() {
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException("not readable",
        new RuntimeException("root cause"), null);

    ResponseEntity<?> response =
        this.advice.handleHttpMessageNotReadableException(ex, this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenConcurrentUpdateException_whenHandleException_thenReturnConflict() {
    ResponseEntity<?> response = this.advice.handleConcurrentUpdateException(
        new ConcurrentUpdateException("some-id", "\"3\""), this.createWebRequest());

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }
}
