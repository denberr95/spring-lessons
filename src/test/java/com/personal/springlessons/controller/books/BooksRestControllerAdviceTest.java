package com.personal.springlessons.controller.books;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.personal.springlessons.exception.books.BookNotFoundException;
import com.personal.springlessons.exception.books.CSVContentValidationException;
import com.personal.springlessons.exception.books.DuplicatedBookException;
import com.personal.springlessons.exception.books.InvalidFileTypeException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class BooksRestControllerAdviceTest {

  private final BooksRestControllerAdvice advice = new BooksRestControllerAdvice();

  private WebRequest createWebRequest() {
    return new ServletWebRequest(new MockHttpServletRequest("GET", "/spring-app/v1/books"));
  }

  @Test
  void givenBookNotFoundException_whenHandleException_thenReturnNotFound() {
    ResponseEntity<?> response =
        this.advice.handleBookNotFoundException(new BookNotFoundException("some-id"));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void givenDuplicatedBookException_whenHandleException_thenReturnConflict() {
    ResponseEntity<?> response = this.advice
        .handleDuplicatedBookException(new DuplicatedBookException("Book Title", "existing-id"));

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void givenInvalidFileTypeException_whenHandleException_thenReturnBadRequest() {
    ResponseEntity<?> response = this.advice.handleInvalidFileTypeException(
        new InvalidFileTypeException("report.txt", List.of(".csv")), this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void givenCSVContentValidationException_whenHandleException_thenReturnBadRequest() {
    ResponseEntity<?> response = this.advice.handleCSVContentValidationException(
        new CSVContentValidationException("books.csv", List.of()), this.createWebRequest());

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
