package com.personal.springlessons.controller;

import java.time.LocalDateTime;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.dto.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.lov.APICategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonRestControllerAdvice {

    @ExceptionHandler(value = {InvalidUUIDException.class})
    public ResponseEntity<InvalidUUIDResponseDTO> handleIllegalArgumentException(
            final InvalidUUIDException exception) {
        log.error(exception.getMessage(), exception);
        InvalidUUIDResponseDTO result = new InvalidUUIDResponseDTO();
        InvalidUUIDResponseDTO.Details details = result.new Details();
        result.setCategory(APICategory.BOOKS);
        result.setTimestamp(LocalDateTime.now());
        result.setMessage(exception.getMessage());
        details.setInvalidId(exception.getId());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
