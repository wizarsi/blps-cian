package com.example.blpscian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizeException extends RuntimeException {
    public AuthorizeException(String message) {
        super(message);
    }
}
