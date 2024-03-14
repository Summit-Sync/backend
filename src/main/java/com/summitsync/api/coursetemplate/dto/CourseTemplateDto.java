package com.summitsync.api.coursetemplate.dto;

import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.coursetemplateprice.dto.CourseTemplatePriceDto;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourseTemplateDto {
    private long id;
    private String acronym;
    private String title;
    private int duration;
    private int numberOfDates;
    private String description;
    private Set<QualificationDto>qualificationList;
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    private Set<CourseTemplatePriceDto> priceList;
    private int numberOfMinutesPerDate;
}
