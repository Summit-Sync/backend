package com.summitsync.api.bff;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class SessionMapper {
    public static Session mapAccessTokenResponseDtoToSession(String sessionId, AccessTokenResponseDto accessTokenResponseDto, String role) {
        return Session.builder()
                .id(sessionId)
                .accessToken(accessTokenResponseDto.getAccessToken())
                .expiresIn(accessTokenResponseDto.getExpiresIn())
                .refreshExpiresIn(accessTokenResponseDto.getRefreshExpiresIn())
                .idToken(accessTokenResponseDto.getIdToken())
                .created(Instant.now().atZone(ZoneOffset.UTC))
                .updated(Instant.now().atZone(ZoneOffset.UTC))
                .refreshToken(accessTokenResponseDto.getRefreshToken())
                .role(role)
                .build();
    }
}
