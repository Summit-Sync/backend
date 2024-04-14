package com.summitsync.api.coursetemplate.dto;

import com.summitsync.api.coursetemplateprice.dto.CourseTemplatePriceDto;
import com.summitsync.api.location.dto.GetLocationDto;
import com.summitsync.api.qualification.dto.QualificationDto;
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
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    private int numberParticipants;
    private int numberWaitlist;
    private GetLocationDto location;
    private String meetingPoint;
    private List<CourseTemplatePriceDto> price;
    private List<QualificationDto> requiredQualifications;
}

// CourseTemplateDTO: id (long), acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location (LocationDTO), meetingPoint (String), price (array von priceDto),
// requiredQulaifications (array von QualificationDto), numberTrainers (int)