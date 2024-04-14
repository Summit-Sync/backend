package com.summitsync.api.grouptemplate.dto;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTemplateGetDTO {
    private long id;
    private String acronym;
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    private GetLocationDto location;
    private String meetingPoint;
    private BigDecimal trainerPricePerHour;
    private BigDecimal pricePerParticipant;
    private Set<QualificationDto> requiredQualificationList;
    private int participantsPerTrainer;
}

// id (long), acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// LocationDTO, meetingPoint (String), TrainerPricePerHour (BigDecimal),
// PricePerParticipant (BigDecimal), requiredQulaificationList (Liste von QualifiationDTOs),
// participantsPerTrainer (int)
