package com.summitsync.api.exceptionhandler;

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

    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<?> handleInvalidSessionException() {
        var errorResponse = new ErrorResponse("invalid_session", "The provided sessionID is invalid");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
        var errorResponse = new ErrorResponse("not_found", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(KeycloakApiException.class)
    public ResponseEntity<?> handleKeycloakApiException(KeycloakApiException keycloakApiException) {
        var errorResponse = new ErrorResponse("keycloak", keycloakApiException.getMessage());
        return new ResponseEntity<>(errorResponse, keycloakApiException.getStatus());
    }
}
