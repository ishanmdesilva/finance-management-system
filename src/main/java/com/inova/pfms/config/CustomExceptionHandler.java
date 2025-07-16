package com.inova.pfms.config;

import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectSearchFailedException;
import com.inova.pfms.exception.ObjectUpdateFailedException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inova.pfms.dto.response.ErrorResponseDTO;
import com.inova.pfms.exception.ObjectAlreadyExistsException;
import com.inova.pfms.exception.ObjectDoesNotExistException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsExceptionHandler(BadCredentialsException e) {
    	log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("AUTH_ERROR", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> objectNotFoundExceptionHandler(ObjectNotFoundException e) {
    	log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<Object> objectAlreadyExistsExceptionHandler(ObjectAlreadyExistsException e) {
    	log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("OBJECT_ALREADY_EXISTS", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentExceptionExceptionHandler(IllegalArgumentException e) {
    	log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("ILLEGAL_ARGUMENT", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ObjectDoesNotExistException.class)
    public ResponseEntity<Object> objectDoesNotExistException(ObjectDoesNotExistException e) {
    	log.error(e.getMessage(), e);
    	return new ResponseEntity<>(new ErrorResponseDTO("NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectCreationFailedException.class)
    public ResponseEntity<Object> objectCreationFailureExceptionHandler(ObjectCreationFailedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("OBJECT_CREATION_FAILED", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectUpdateFailedException.class)
    public ResponseEntity<Object> objectUpdateFailedExceptionHandler(ObjectCreationFailedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("OBJECT_UPDATE_FAILED", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectSearchFailedException.class)
    public ResponseEntity<Object> objectSearchFailedExceptionHandler(ObjectSearchFailedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("SEARCH_OBJECTS_FAILED", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(
                new ErrorResponseDTO("VALIDATION_FAILED", errors.toString()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> allOtherExceptionHandler(Exception e) {
    	log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponseDTO("INTERNAL_ERROR", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
