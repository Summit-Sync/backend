package com.summitsync.api.group.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class GroupPostDTO {
    String title;
    String description;
    int numberOfDates;
    List<LocalDateTime> dates;
    int duration;
    int numberParticipants;
    long contact;
    long location;
    String meetingPoint;
    BigDecimal trainerPricePerHour;
    BigDecimal pricePerParticipant;
    Set<Integer> requiredQualifications;
    int participantsPerTrainer;
    Set<Integer> trainers;

}

// PostGroupDTO: title (string), description (string), numberOfDates (int), Array of eventDateObj (date (date)), duration (int),
// numberParticipants (int), PostContactDTO, LocationDTO, meetingPoint (String), TrainerPricePerHour (BigDecimal), PricePerParticipant (BigDecimal),
// requiredQulaificationList (Liste von QualifiationDTOs), participantsPerTrainer (int), trainerList (Liste von Trainer DTOs)