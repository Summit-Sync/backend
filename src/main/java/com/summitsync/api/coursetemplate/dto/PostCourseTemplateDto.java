package com.summitsync.api.coursetemplate.dto;

import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.qualification.Qualification;
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
    private int duration;
    private int numberOfDates;
    private String description;
    private List<Qualification>qualificationList;
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    private List<CourseTemplatePrice>priceList;
    private int numberOfMinutesPerDate;
}
