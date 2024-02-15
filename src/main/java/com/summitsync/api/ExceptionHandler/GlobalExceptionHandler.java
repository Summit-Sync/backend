package com.summitsync.api.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CodeExchangeException.class)
    public ResponseEntity<?> handleCodeExchangeException(CodeExchangeException exception) {
        var errorResponse = new ErrorResponse("handleCodeExchangeException", "failed to exchange code for access token: " + exception.getMessage());
        return new ResponseEntity<>(errorResponse, exception.getStatus());
    }
}
