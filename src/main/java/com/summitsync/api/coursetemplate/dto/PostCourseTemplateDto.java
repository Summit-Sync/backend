package com.summitsync.api.coursetemplate.dto;

import com.summitsync.api.price.dto.PricePostDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCourseTemplateDto {
    @NotNull
    @Size(max = 3, min = 2, message = "acronym has to be 2 or 3 long")
    private String acronym;
    @NotBlank(message = "acronym cannot be empty")
    private String title;
    private String description;
    @NotNull
    @Positive(message = "more than 1 dates needed")
    private int numberOfDates;
    @NotNull
    @Positive(message = "duration cannot be negative")
    private int duration;
    @NotNull
    @Positive(message = "the number of participants has to be positive")
    private int numberParticipants;
    @NotNull
    @PositiveOrZero(message = "the number of participants on the waitlist can not be negative")
    private int numberWaitlist;
    private Long location;
    private String meetingPoint;
    private List<PricePostDto> price;
    private List<Long> requiredQualifications;
    @NotNull
    @PositiveOrZero(message = "the number of trainers has to be more than 0")
    private int numberTrainers;
}

//PostCourseTemplateDTO: acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location: Location (int), meetingPoint (String), price: Price (array von int),
// requiredQulaifications (array von int), numberTrainers (int)
