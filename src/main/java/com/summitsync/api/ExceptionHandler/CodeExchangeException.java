package com.summitsync.api.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CodeExchangeException extends RuntimeException {
    private HttpStatus status;

    public CodeExchangeException(String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.status = status;
    }
}
