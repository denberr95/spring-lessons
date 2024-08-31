package com.personal.springlessons.controller;


import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.dto.InvalidUUIDResponseDTO;
import com.personal.springlessons.model.dto.UnauthorizedResponseDTO;
import com.personal.springlessons.model.lov.DomainCategory;
import com.personal.springlessons.util.Methods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonRestControllerAdvice {

    @ExceptionHandler(value = {InvalidUUIDException.class})
    public ResponseEntity<InvalidUUIDResponseDTO> handleIllegalArgumentException(
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

    @ExceptionHandler(value = {AuthorizationDeniedException.class})
    public ResponseEntity<UnauthorizedResponseDTO> handleAuthorizationDeniedException(
            AuthorizationDeniedException exception, WebRequest webRequest,
            HandlerMethod handlerMethod) {
        log.error(exception.getMessage(), exception);
        UnauthorizedResponseDTO result = new UnauthorizedResponseDTO();
        UnauthorizedResponseDTO.Details details = result.new Details();
        result.setMessage("Access denied to resource");
        result.setCategory(DomainCategory.AUTHORIZATION);
        details.setResource(webRequest.getDescription(false).replace("uri=", ""));
        result.setAdditionalData(details);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
