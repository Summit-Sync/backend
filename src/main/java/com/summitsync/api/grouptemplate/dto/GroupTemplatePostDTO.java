package com.summitsync.api.grouptemplate.dto;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.qualification.Qualification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupTemplatePostDTO {
    @NotBlank(message = "Acronym cannot be empty.")
    private String acronym;
    @NotBlank(message = "Title cannot be empty.")
    private String title;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    @NotNull(message = "Number of Dates cannot be empty.")
    @PositiveOrZero(message = "Number of Dates must be positive or zero.")
    private int numberOfDates;
    @NotNull(message = "Duration cannot be empty.")
    @PositiveOrZero(message = "Duration must be positive or zero.")
    private int duration;
    @NotNull(message = "Location cannot be empty.")
    private long location;
    @NotBlank(message = "Meeting Point cannot be empty.")
    private String meetingPoint;
    @NotNull(message = "Trainer Price per Hour cannot be empty.")
    @PositiveOrZero(message = "Trainer Price per Hour must be positive or zero.")
    private BigDecimal trainerPricePerHour;
    @NotNull(message = "Price per Participant cannot be empty.")
    @PositiveOrZero(message = "Price per Participant must be positive or zero.")
    private BigDecimal pricePerParticipant;
    private List<Long> requiredQualificationList;
    @NotNull(message = "Participants per Trainer cannot be empty.")
    @PositiveOrZero(message = "Participants per Trainer must be positive or zero.")
    private int participantsPerTrainer;
}
