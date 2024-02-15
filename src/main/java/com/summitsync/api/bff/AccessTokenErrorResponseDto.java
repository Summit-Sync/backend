package com.summitsync.api.bff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

// https://datatracker.ietf.org/doc/html/rfc6749#section-5.2
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenErrorResponseDto {
    private String error;
    private String error_description;
    private String error_uri;
}
