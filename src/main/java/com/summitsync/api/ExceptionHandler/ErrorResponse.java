package com.summitsync.api.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ErrorResponse {
    String error;
    String details;
}
