package com.summitsync.api.group.dto;

import com.summitsync.api.contact.dto.ContactPostDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @NotNull(message = "Number of Dates cannot be empty.")
    @PositiveOrZero(message = "Number of Dates must be positive or zero.")
    int numberOfDates;
    @NotNull(message = "Dates cannot be empty.")
    List<LocalDateTime> dates;
    @NotNull(message = "Duration cannot be empty.")
    @PositiveOrZero(message = "Duration must be positive or zero.")
    int duration;
    @NotNull(message = "Number of participants cannot be empty.")
    @PositiveOrZero(message = "Number of participants must be positive or zero.")
    int numberParticipants;
    @NotNull(message = "Contact cannot be empty.")
    ContactPostDto contact;
    @NotNull(message = "Location cannot be empty.")
    long location;
    @NotNull(message = "Meeting Point cannot be empty.")
    String meetingPoint;
    @NotNull(message = "Trainer Price per Hour cannot be empty.")
    @PositiveOrZero(message = "Trainer Price per hour must be positive or zero.")
    BigDecimal trainerPricePerHour;
    @NotNull(message = "Price per Participant cannot be empty.")
    @PositiveOrZero(message = "Price per Participant must be positive or zero.")
    BigDecimal pricePerParticipant;
    @NotNull(message = "Required qualifications cannot be empty.")
    Set<Integer> requiredQualifications;
    @NotNull(message = "Participants per Trainer cannot be empty.")
    @PositiveOrZero(message = "Participants per Trainer must be positive or zero.")
    int participantsPerTrainer;
    @NotNull(message = "Trainers cannot be empty.")
    Set<Integer> trainers;
    @NotBlank(message = "Acronym cannot be empty")
    @Length(max = 2, message = "Acronym cant be longer than 2")
    String acronym;
    String notes;
}

// PostGroupDTO: title (string), description (string), numberOfDates (int), Array of eventDateObj (date (date)), duration (int),
// numberParticipants (int), PostContactDTO, LocationDTO, meetingPoint (String), TrainerPricePerHour (BigDecimal), PricePerParticipant (BigDecimal),
// requiredQulaificationList (Liste von QualifiationDTOs), participantsPerTrainer (int), trainerList (Liste von Trainer DTOs)