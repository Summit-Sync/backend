package com.summitsync.api.bff;

import java.time.LocalDateTime;

public class SessionMapper {
    public static Session mapAccessTokenResponseDtoToSession(String sessionId, AccessTokenResponseDto accessTokenResponseDto, String role) {
        return Session.builder()
                .id(sessionId)
                .accessToken(accessTokenResponseDto.getAccessToken())
                .expiresIn(accessTokenResponseDto.getExpiresIn())
                .refreshExpiresIn(accessTokenResponseDto.getRefreshExpiresIn())
                .idToken(accessTokenResponseDto.getIdToken())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .refreshToken(accessTokenResponseDto.getRefreshToken())
                .role(role)
                .build();
    }
}
