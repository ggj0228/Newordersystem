package com.example.ordersystem.common.service;

import com.example.ordersystem.common.dto.response.CommonErrrorResponse;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> duplicateDbError(EntityExistsException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new CommonErrrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validError(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(new CommonErrrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> otherValueError(IllegalArgumentException e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(new CommonErrrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);

    }
}
