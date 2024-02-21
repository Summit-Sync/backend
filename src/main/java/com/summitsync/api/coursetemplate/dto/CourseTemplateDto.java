package com.summitsync.api.coursetemplate.dto;

import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.qualification.Qualification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourseTemplateDto {
    private long id;
    private String acronym;
    private int numberOfDates;
    private String description;
    private List<Qualification>qualificationList;
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    private List<CourseTemplatePrice> priceList;
}
