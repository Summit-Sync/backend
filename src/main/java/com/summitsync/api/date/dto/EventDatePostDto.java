package com.summitsync.api.date.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDatePostDto {
    @NotBlank(message = "Start Time cannot be empty.")
    private LocalDateTime startTime;
    @NotBlank(message = "Duration in minutes cannot be empty.")
    @PositiveOrZero(message = "The duration must be positive or zero.")
    private int durationInMinutes;
}
