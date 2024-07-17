package com.personal.springlessons.controller;

import java.time.LocalDateTime;
import com.personal.springlessons.exception.BookNotFoundException;
import com.personal.springlessons.exception.DuplicatedBookException;
import com.personal.springlessons.model.dto.BookNotFoundResponseDTO;
import com.personal.springlessons.model.dto.DuplicatedBookResponseDTO;
import com.personal.springlessons.model.lov.APICategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = BookRestController.class)
public class BookRestControllerAdvice {

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<BookNotFoundResponseDTO> handleBookNotFoundException(
            final BookNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        BookNotFoundResponseDTO result = new BookNotFoundResponseDTO();
        result.setId(exception.getId());
        result.setCategory(APICategory.BOOKS);
        result.setMessage(exception.getMessage());
        result.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(value = {DuplicatedBookException.class})
    public ResponseEntity<DuplicatedBookResponseDTO> handleDuplicatedBookException(
            final DuplicatedBookException exception) {
        log.error(exception.getMessage(), exception);
        DuplicatedBookResponseDTO result = new DuplicatedBookResponseDTO();
        DuplicatedBookResponseDTO.Details details = result.new Details();
        result.setCategory(APICategory.BOOKS);
        result.setTimestamp(LocalDateTime.now());
        result.setMessage(exception.getMessage());
        details.setOrinalId(exception.getId());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }
}
