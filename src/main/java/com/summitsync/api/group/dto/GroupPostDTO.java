package com.summitsync.api.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class GroupPostDTO {
    @NotBlank(message = "Title cannot be empty.")
    String title;
    @NotBlank(message = "Description cannot be empty.")
    String description;
    @NotBlank(message = "Number of Dates cannot be empty.")
    @PositiveOrZero(message = "Number of Dates must be positive or zero.")
    int numberOfDates;
    @NotBlank(message = "Dates cannot be empty.")
    List<LocalDateTime> dates;
    @NotBlank(message = "Duration cannot be empty.")
    @PositiveOrZero(message = "Duration must be positive or zero.")
    int duration;
    @NotBlank(message = "Number of participants cannot be empty.")
    @PositiveOrZero(message = "Number of participants must be positive or zero.")
    int numberParticipants;
    @NotBlank(message = "Contact cannot be empty.")
    long contact;
    @NotBlank(message = "Location cannot be empty.")
    long location;
    @NotBlank(message = "Meeting Point cannot be empty.")
    String meetingPoint;
    @NotBlank(message = "Trainer Price per Hour cannot be empty.")
    @PositiveOrZero(message = "Trainer Price per hour must be positive or zero.")
    BigDecimal trainerPricePerHour;
    @NotBlank(message = "Price per Participant cannot be empty.")
    @PositiveOrZero(message = "Price per Participant must be positive or zero.")
    BigDecimal pricePerParticipant;
    @NotBlank(message = "Required qualifications cannot be empty.")
    Set<Integer> requiredQualifications;
    @NotBlank(message = "Participants per Trainer cannot be empty.")
    @PositiveOrZero(message = "Participants per Trainer must be positive or zero.")
    int participantsPerTrainer;
    @NotBlank(message = "Trainers cannot be empty.")
    Set<Integer> trainers;

}

// PostGroupDTO: title (string), description (string), numberOfDates (int), Array of eventDateObj (date (date)), duration (int),
// numberParticipants (int), PostContactDTO, LocationDTO, meetingPoint (String), TrainerPricePerHour (BigDecimal), PricePerParticipant (BigDecimal),
// requiredQulaificationList (Liste von QualifiationDTOs), participantsPerTrainer (int), trainerList (Liste von Trainer DTOs)