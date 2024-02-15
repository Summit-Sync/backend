package com.summitsync.api.bff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponseDto {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String id_token;
}
