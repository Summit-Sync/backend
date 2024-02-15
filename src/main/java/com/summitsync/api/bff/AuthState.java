package com.summitsync.api.bff;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "AuthState", timeToLive = 600)
@Getter
@Setter
@AllArgsConstructor
public class AuthState {
    private String id;
    private String redirectUri;
    private String codeVerifier;
}
