package com.summitsync.api.grouptemplate.dto;

import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupTemplatePostDTO {
    private String acronym;
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    private long location;
    private String meetingPoint;
    private BigDecimal trainerPricePerHour;
    private BigDecimal pricePerParticipant;
    private List<Long> requiredQualificationList;
    private int participantsPerTrainer;
}
