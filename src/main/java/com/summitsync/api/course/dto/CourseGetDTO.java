package com.summitsync.api.course.dto;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.dto.EventDateGetDto;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseGetDTO {
    private long id;
    private CourseTemplate template;
    private String description;
    private String title;
    private List<QualificationDto> requiredQualifications;
    private List<ParticipantDto> participants;
    private String notes;
    private List<EventDateGetDto> dates;
    private BigDecimal actualPrice;
    private Location location;
}
