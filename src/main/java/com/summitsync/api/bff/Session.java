package com.summitsync.api.bff;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@RedisHash("Session")
@Getter
@Setter
@AllArgsConstructor
public class Session {
    @Id
    private String id;
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String idToken;
    private LocalDateTime created;
    @TimeToLive
    public Long getTimeToLive() {
        return (long) this.expiresIn;
    }
}
