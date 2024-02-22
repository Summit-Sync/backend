package com.summitsync.api.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ErrorResponse {
    String error;
    String details;
}
