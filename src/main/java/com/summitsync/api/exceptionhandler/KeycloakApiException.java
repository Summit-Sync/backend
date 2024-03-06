package com.summitsync.api.exceptionhandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class KeycloakApiException extends RuntimeException {
    private HttpStatus status;

    public KeycloakApiException(String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.status = status;
    }
}
