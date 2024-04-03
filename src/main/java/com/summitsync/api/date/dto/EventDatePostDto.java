package com.summitsync.api.date.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDatePostDto {
    private LocalDateTime startTime;
    private int durationInMinutes;
}
