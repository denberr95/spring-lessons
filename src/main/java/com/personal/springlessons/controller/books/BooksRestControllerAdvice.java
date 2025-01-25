package com.personal.springlessons.controller.books;

import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.CSVContentValidationException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.exception.InvalidFileTypeException;
import com.personal.springlessons.model.dto.response.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.response.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidCSVContentResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidFileTypeResponseDTO;
import com.personal.springlessons.model.lov.DomainCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = BooksRestController.class)
public class BooksRestControllerAdvice {

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<BookNotFoundResponseDTO> handleBookNotFoundException(
            BookNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        BookNotFoundResponseDTO result = new BookNotFoundResponseDTO();
        BookNotFoundResponseDTO.Details details = result.new Details();
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
        DuplicatedBookResponseDTO.Details details = result.new Details();
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
        InvalidFileTypeResponseDTO.Details details = result.new Details();
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
        InvalidCSVContentResponseDTO.Details details = result.new Details();
        result.setCategory(DomainCategory.BOOKS);
        result.setMessage(exception.getMessage());
        result.setTotalRows(exception.getRows().size());
        details.setRows(exception.getRows());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
