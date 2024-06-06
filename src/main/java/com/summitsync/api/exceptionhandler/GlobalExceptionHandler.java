package com.summitsync.api.exceptionhandler;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?>  handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errorMsg = new StringBuilder();
        errorMsg.append("Error(s) occurred during validation:\n");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMsg.append(fieldName).append(": ").append(errorMessage).append("\n");
        });
        var errorResponse = new ErrorResponse("validation", errorMsg.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = NestedExceptionUtils.getMostSpecificCause(exception).getMessage();
        var errorResponse = new ErrorResponse("dataIntegrityViolation", message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
