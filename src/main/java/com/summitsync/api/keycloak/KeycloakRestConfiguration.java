package com.summitsync.api.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "summitsync.keycloak")
@Getter
@Setter
public class KeycloakRestConfiguration {
    private String restEndpoint;
    private String systemUserUsername;
    private String systemUserPassword;
}
