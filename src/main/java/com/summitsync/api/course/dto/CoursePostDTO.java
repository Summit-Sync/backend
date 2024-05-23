package com.summitsync.api.course.dto;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.dto.EventDatePostDto;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePostDTO {
    @NotNull(message = "Visible cannot be empty.")
    private boolean visible;
    @NotBlank(message = "Acronym cannot be empty.")
    private String acronym;
    @NotBlank(message = "Description cannot be empty.")
    private String description;
    @NotEmpty(message = "Dates cannot be empty.")
    private List<LocalDateTime> dates;
    @NotNull(message = "Duration cannot be empty.")
    @PositiveOrZero(message = "Duration must be positive or zero.")
    private int duration;
    @NotNull(message = "Number of Participants cannot be empty.")
    @PositiveOrZero(message = "Number of Participants must be positive or zero.")
    private int numberParticipants;
    @NotNull(message = "Number of Waitlist cannot be empty.")
    @PositiveOrZero(message = "Number of Waitlist must be positive or zero.")
    private int numberWaitlist;
    @NotEmpty(message = "Prices cannot be empty.")
    private List<Long> prices;
    private long location;
    @NotBlank(message = "Meeting Point cannot be empty.")
    private String meetingPoint;
    private List<Long> requiredQualifications;
    @NotNull(message = "Number of Trainers cannot be empty.")
    @PositiveOrZero(message = "Number of Trainers must be positive or zero.")
    private int numberTrainers;
    private String notes;
    @NotBlank(message = "Title cannot be empty.")
    private String title;
    private List<ParticipantDto> participants;
    private List<TrainerDto> trainers;
    private List<ParticipantDto> waitList;
}

// PostCourseDTO: visible (boolean), acronym (string), description (String), dates (Date als string), duration(int),
// numberParticipants(int), numberWaitlist(int), prices (array von int),location (int), meetingPoint (String),
// requiredQulaifications (array von int), numberTrainers (int), notes (String)