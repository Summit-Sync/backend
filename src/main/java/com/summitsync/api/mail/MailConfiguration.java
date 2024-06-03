package com.summitsync.api.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "summitsync.mail")
@Getter
@Setter
public class MailConfiguration {
    private boolean enabled;
}
