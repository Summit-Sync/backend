package com.summitsync.api.coursetemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCourseTemplateDto {
    private String acronym;
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    private int numberParticipants;
    private int numberWaitlist;
    private long location;
    private String meetingPoint;
    private List<Long> price;
    private List<Long> requiredQualifications;
    private int numberTrainers;
}

//PostCourseTemplateDTO: acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location: Location (int), meetingPoint (String), price: Price (array von int),
// requiredQulaifications (array von int), numberTrainers (int)
