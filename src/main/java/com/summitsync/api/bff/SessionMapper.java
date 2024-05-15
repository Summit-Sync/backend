package com.summitsync.api.bff;

import java.time.LocalDateTime;

public class SessionMapper {
    public static Session mapAccessTokenResponseDtoToSession(String sessionId, AccessTokenResponseDto accessTokenResponseDto, String role) {
        return new Session(sessionId,
                accessTokenResponseDto.getAccess_token(),
                accessTokenResponseDto.getExpires_in(),
                accessTokenResponseDto.getRefresh_token(),
                accessTokenResponseDto.getId_token(),
                LocalDateTime.now(),
                role
        );
    }
}
