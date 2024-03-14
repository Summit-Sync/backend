package com.summitsync.api.course.dto;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.dto.EventDatePostDto;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CoursePostDTO {
    private CourseTemplate template;
    private String description;
    private String title;
    private List<Long> requiredQualificationIds;
    private String notes;
    private List<EventDatePostDto> dates;
    private BigDecimal actualPrice;
    private Location location;
}
