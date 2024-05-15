package com.summitsync.api.bff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BffAccessTokenResponse {
    String accessToken;
    long expiresIn;
    String role;
}
