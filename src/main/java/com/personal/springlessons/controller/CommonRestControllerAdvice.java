package com.personal.springlessons.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.validation.ConstraintViolationException;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.model.dto.response.GenericErrorAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.GenericErrorResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidArgumentTypeAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.InvalidArgumentTypeResponseDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.dto.response.MissingHttpRequestHeaderAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.MissingHttpRequestHeaderResponseDTO;
import com.personal.springlessons.model.dto.response.NotReadableBodyRequestAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.NotReadableBodyRequestResponseDTO;
import com.personal.springlessons.model.dto.response.ValidationRequestAdditionalDetailsDTO;
import com.personal.springlessons.model.dto.response.ValidationRequestErrorResponseDTO;
import com.personal.springlessons.util.Constants;
import com.personal.springlessons.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CommonRestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(CommonRestControllerAdvice.class);

    @ExceptionHandler(value = {InvalidUUIDException.class})
    public ResponseEntity<InvalidUUIDResponseDTO> handleInvalidUUIDException(
            InvalidUUIDException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        InvalidUUIDResponseDTO result = new InvalidUUIDResponseDTO();
        InvalidUUIDAdditionalDetailsDTO details = new InvalidUUIDAdditionalDetailsDTO();
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
        GenericErrorAdditionalDetailsDTO details = new GenericErrorAdditionalDetailsDTO();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Generic exception !");
        details.setExceptionMessage(exception.getMessage());
        details.setExceptionName(exception.getClass().getName());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationRequestErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        ValidationRequestErrorResponseDTO result = new ValidationRequestErrorResponseDTO();
        List<ValidationRequestAdditionalDetailsDTO> details = new ArrayList<>();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Invalid request !");
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ValidationRequestAdditionalDetailsDTO detail = new ValidationRequestAdditionalDetailsDTO();
            detail.setField(fieldError.getField());
            detail.setMessage(fieldError.getDefaultMessage());
            details.add(detail);
        });
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<MissingHttpRequestHeaderResponseDTO> handleMissingRequestHeaderException(
            MissingRequestHeaderException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        MissingHttpRequestHeaderResponseDTO result = new MissingHttpRequestHeaderResponseDTO();
        MissingHttpRequestHeaderAdditionalDetailsDTO details = new MissingHttpRequestHeaderAdditionalDetailsDTO();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Missing http request header !");
        details.setHeader(exception.getHeaderName());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<InvalidArgumentTypeResponseDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        InvalidArgumentTypeResponseDTO result = new InvalidArgumentTypeResponseDTO();
        InvalidArgumentTypeAdditionalDetailsDTO details = new InvalidArgumentTypeAdditionalDetailsDTO();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Invalid data type !");
        details.setField(exception.getName());
        details.setValue(exception.getValue() != null ? exception.getValue().toString() : null);
        if (exception.getRequiredType() != null && exception.getRequiredType().isEnum()) {
            Object[] enumValues = exception.getRequiredType().getEnumConstants();
            if (enumValues != null) {
                List<String> enumValueStrings =
                        Arrays.stream(enumValues).map(Object::toString).toList();
                details.setPickList(enumValueStrings);
            }
        }
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ValidationRequestErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        ValidationRequestErrorResponseDTO result = new ValidationRequestErrorResponseDTO();
        List<ValidationRequestAdditionalDetailsDTO> details = new ArrayList<>();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Validation request not passed !");
        exception.getConstraintViolations().forEach(constraintViolation -> {
            ValidationRequestAdditionalDetailsDTO detail = new ValidationRequestAdditionalDetailsDTO();
            String propertyPath = constraintViolation.getPropertyPath() != null
                    ? constraintViolation.getPropertyPath().toString()
                    : null;
            if (propertyPath != null && propertyPath.contains(Constants.S_DOT)) {
                propertyPath = propertyPath.substring(propertyPath.indexOf(Constants.S_DOT) + 1);
            }
            detail.setField(propertyPath);
            detail.setMessage(constraintViolation.getMessage());
            detail.setValue(constraintViolation.getInvalidValue() != null
                    ? constraintViolation.getInvalidValue().toString()
                    : null);
            details.add(detail);
        });
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<NotReadableBodyRequestResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception, WebRequest webRequest) {
        log.error(exception.getMessage(), exception);
        NotReadableBodyRequestResponseDTO result = new NotReadableBodyRequestResponseDTO();
        NotReadableBodyRequestAdditionalDetailsDTO details = new NotReadableBodyRequestAdditionalDetailsDTO();
        result.setCategory(Methods.retrieveDomainCategory(webRequest.getDescription(false)));
        result.setMessage("Body request not processable !");
        details.setException(exception.getRootCause().getLocalizedMessage());
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
