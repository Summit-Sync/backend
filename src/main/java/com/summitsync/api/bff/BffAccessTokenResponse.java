package com.summitsync.api.bff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BffAccessTokenResponse {
    String accessToken;
    ZonedDateTime expiresAt;
    ZonedDateTime refreshTokenExpiresAt;
    String role;
}
