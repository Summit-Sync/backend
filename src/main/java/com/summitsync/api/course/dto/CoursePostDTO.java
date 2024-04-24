package com.summitsync.api.course.dto;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.dto.EventDatePostDto;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CoursePostDTO {
    @NotBlank(message = "Visible cannot be empty.")
    private boolean visible;
    @NotBlank(message = "Acronym cannot be empty.")
    private String acronym;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    @NotBlank(message = "Dates cannot be empty.")
    private List<LocalDateTime> dates;
    @NotBlank(message = "Duration cannot be empty.")
    @PositiveOrZero(message = "Duration must be positive or zero.")
    private int duration;
    @NotBlank(message = "Number of Participants cannot be empty.")
    @PositiveOrZero(message = "Number of Participants must be positive or zero.")
    private int numberParticipants;
    @NotBlank(message = "Number of Waitlist cannot be empty.")
    @PositiveOrZero(message = "Number of Waitlist must be positive or zero.")
    private int numberWaitlist;
    @NotBlank(message = "Prices cannot be empty.")
    private List<Long> prices;
    @NotBlank(message = "Location cannot be empty.")
    private long location;
    @NotBlank(message = "Meeting Point cannot be empty.")
    private String meetingPoint;
    @NotBlank(message = "Required Qualifications cannot be empty.")
    private List<Long> requiredQualifications;
    @NotBlank(message = "Number of Trainers cannot be empty.")
    @PositiveOrZero(message = "Number of Trainers must be positive or zero.")
    private int numberTrainers;
    private String notes;
    @NotBlank(message = "Title cannot be empty.")
    private String title;
}

// PostCourseDTO: visible (boolean), acronym (string), description (String), dates (Date als string), duration(int),
// numberParticipants(int), numberWaitlist(int), prices (array von int),location (int), meetingPoint (String),
// requiredQulaifications (array von int), numberTrainers (int), notes (String)