package com.summitsync.api.coursetemplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCourseTemplateDto {
    @Size(max = 3, min = 2, message = "acronym has to be 2 or 3 long")
    private String acronym;
    @NotBlank(message = "acronym cannot be empty")
    private String title;
    private String description;
    @Positive(message = "more than 1 dates needed")
    private int numberOfDates;
    @Positive(message = "duration cannot be negative")
    private int duration;
    @Positive(message = "the number of participants has to be positive")
    private int numberParticipants;
    @PositiveOrZero(message = "the number of participants on the waitlist can not be negative")
    private int numberWaitlist;
    private Long location;
    private String meetingPoint;
    private List<Long> price;
    private List<Long> requiredQualifications;
    @PositiveOrZero(message = "the number of trainers has to be more than 0")
    private int numberTrainers;
}

//PostCourseTemplateDTO: acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location: Location (int), meetingPoint (String), price: Price (array von int),
// requiredQulaifications (array von int), numberTrainers (int)
