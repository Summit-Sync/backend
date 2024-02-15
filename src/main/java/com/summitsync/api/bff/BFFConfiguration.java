package com.summitsync.api.bff;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "summitsync.bff")
@Getter
@Setter
public class BFFConfiguration {
    private String authorizationUrl;
    private String tokenEndpoint;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
