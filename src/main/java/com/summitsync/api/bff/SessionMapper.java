package com.summitsync.api.bff;

import java.time.LocalDateTime;

public class SessionMapper {
    public static Session MapAccessTokenResponseDtoToSession(String sessionId, AccessTokenResponseDto accessTokenResponseDto) {
        return new Session(sessionId,
                accessTokenResponseDto.getAccess_token(),
                accessTokenResponseDto.getExpires_in(),
                accessTokenResponseDto.getRefresh_token(),
                accessTokenResponseDto.getId_token(),
                LocalDateTime.now()
        );
    }
}
