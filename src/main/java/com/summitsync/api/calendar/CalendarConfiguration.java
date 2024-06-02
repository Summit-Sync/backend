package com.summitsync.api.calendar;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "summitsync.calendar")
@Getter
@Setter
public class CalendarConfiguration {
    private boolean enabled;
    private String calendarUrl;
    private String serviceAccountJsonPath;
}
