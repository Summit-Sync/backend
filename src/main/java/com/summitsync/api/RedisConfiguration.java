package com.summitsync.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "summitsync.redis")
@Getter
@Setter
public class RedisConfiguration {
    private String host;
}
