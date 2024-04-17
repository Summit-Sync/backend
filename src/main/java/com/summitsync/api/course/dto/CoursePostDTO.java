package com.summitsync.api.course.dto;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.dto.EventDatePostDto;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CoursePostDTO {
    private boolean visible;
    private String acronym;
    private String description;
    private List<LocalDateTime> dates;
    private int duration;
    private int numberParticipants;
    private int numberWaitlist;
    private List<Long> prices;
    private long location;
    private String meetingPoint;
    private List<Long> requiredQualifications;
    private int numberTrainers;
    private String notes;
    private String title;
}

// PostCourseDTO: visible (boolean), acronym (string), description (String), dates (Date als string), duration(int),
// numberParticipants(int), numberWaitlist(int), prices (array von int),location (int), meetingPoint (String),
// requiredQulaifications (array von int), numberTrainers (int), notes (String)