package com.summitsync.api.date.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDateGetDto {
    private long eventDateId;
    private LocalDateTime startTime;
    private int durationInMinutes;
}
