package com.summitsync.api.group.dto;

import com.summitsync.api.contact.dto.ContactGetDto;
import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.qualification.dto.QualificationDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupGetDTO {
    long id;
    boolean cancelled;
    String groupNumber;
    String title;
    String description;
    int numberOfDates;
    int duration;
    ContactGetDto contact;
    Set<LocalDateTime> dates;
    int numberParticipants;
    GetLocationDto location;
    String meetingPoint;
    BigDecimal trainerPricePerHour;
    BigDecimal pricePerParticipant;
    Set<QualificationDto> requiredQualifications;
    int participantsPerTrainer;
    Set<TrainerDto> trainers;
    BigDecimal totalPrice;
}
