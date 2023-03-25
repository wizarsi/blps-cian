package com.example.blpscian.controllers;

import com.example.blpscian.exceptions.AuthorizeException;
import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.exceptions.NoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionApiHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchUserException.class)
    protected ResponseEntity<Object> NoSuchUser(NoSuchUserException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    protected ResponseEntity<Object> InvalidData(InvalidDataException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(AuthorizeException.class)
    protected ResponseEntity<Object> Authorize(AuthorizeException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
