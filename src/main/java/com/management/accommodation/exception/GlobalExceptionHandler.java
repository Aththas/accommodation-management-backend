package com.management.accommodation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleObjectNotValidException(ObjectNotValidException e){
        log.error(e.getErrorMsg().toString());
        return new ResponseEntity<>(e.getErrorMsg(), HttpStatus.BAD_REQUEST);
    }
}
