package com.personal.springlessons.controller;


import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDResponseDTO;
import com.personal.springlessons.util.Methods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonRestControllerAdvice {

    @ExceptionHandler(value = {InvalidUUIDException.class})
    public ResponseEntity<InvalidUUIDResponseDTO> handleInvalidUUIDException(
            InvalidUUIDException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        InvalidUUIDResponseDTO result = new InvalidUUIDResponseDTO();
        InvalidUUIDResponseDTO.Details details = result.new Details();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage(exception.getMessage());
        details.setInvalidId(exception.getId());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = {SpringLessonsApplicationException.class})
    public ResponseEntity<GenericErrorResponseDTO> handleSpringLessonsApplicationException(
            SpringLessonsApplicationException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        GenericErrorResponseDTO result = new GenericErrorResponseDTO();
        GenericErrorResponseDTO.Details details = result.new Details();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Generic exception !");
        details.setExceptionMessage(exception.getMessage());
        details.setExceptionName(exception.getClass().getName());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
