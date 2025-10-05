package com.personal.springlessons.controller.books;

import com.personal.springlessons.exception.books.BookNotFoundException;
import com.personal.springlessons.exception.books.CSVContentValidationException;
import com.personal.springlessons.exception.books.DuplicatedBookException;
import com.personal.springlessons.exception.books.InvalidFileTypeException;
import com.personal.springlessons.model.dto.response.BookNotFoundAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidCSVContentAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.InvalidCSVContentResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidFileTypeAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.InvalidFileTypeResponseDTO;
import com.personal.springlessons.model.lov.DomainCategory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(assignableTypes = BooksRestController.class)
public class BooksRestControllerAdvice {

  private static final Logger log = LoggerFactory.getLogger(BooksRestControllerAdvice.class);

  @ExceptionHandler(value = {BookNotFoundException.class})
  public ResponseEntity<BookNotFoundResponseDTO> handleBookNotFoundException(
      BookNotFoundException exception) {
    log.error(exception.getMessage(), exception);
    BookNotFoundResponseDTO result = new BookNotFoundResponseDTO();
    BookNotFoundAdditionalDetailsDTO details = new BookNotFoundAdditionalDetailsDTO();
    details.setId(exception.getId());
    result.setCategory(DomainCategory.BOOKS);
    result.setMessage(exception.getMessage());
    result.setAdditionalData(details);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
  }

  @ExceptionHandler(value = {DuplicatedBookException.class})
  public ResponseEntity<DuplicatedBookResponseDTO> handleDuplicatedBookException(
      DuplicatedBookException exception) {
    log.error(exception.getMessage(), exception);
    DuplicatedBookResponseDTO result = new DuplicatedBookResponseDTO();
    DuplicatedBookAdditionalDetailsDTO details = new DuplicatedBookAdditionalDetailsDTO();
    result.setCategory(DomainCategory.BOOKS);
    result.setMessage(exception.getMessage());
    details.setOrinalId(exception.getId());
    result.setAdditionalData(details);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
  }

  @ExceptionHandler(value = {InvalidFileTypeException.class})
  public ResponseEntity<InvalidFileTypeResponseDTO> handleInvalidFileTypeException(
      InvalidFileTypeException exception, WebRequest webRequest) {
    log.error(exception.getMessage(), exception);
    InvalidFileTypeResponseDTO result = new InvalidFileTypeResponseDTO();
    InvalidFileTypeAdditionalDetailsDTO details = new InvalidFileTypeAdditionalDetailsDTO();
    result.setCategory(DomainCategory.BOOKS);
    result.setMessage(exception.getMessage());
    details.setFileName(exception.getFileName());
    details.setValidFileTypes(exception.getValidFileTypes());
    result.setAdditionalData(details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
  }

  @ExceptionHandler(value = {CSVContentValidationException.class})
  public ResponseEntity<InvalidCSVContentResponseDTO> handleCSVContentValidationException(
      CSVContentValidationException exception, WebRequest webRequest) {
    log.error(exception.getMessage(), exception);
    InvalidCSVContentResponseDTO result = new InvalidCSVContentResponseDTO();
    InvalidCSVContentAdditionalDetailsDTO details = new InvalidCSVContentAdditionalDetailsDTO();
    result.setCategory(DomainCategory.BOOKS);
    result.setMessage(exception.getMessage());
    result.setTotalRows(exception.getRows().size());
    details.setRows(exception.getRows());
    result.setAdditionalData(details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
  }
}
