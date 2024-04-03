package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.coursetemplateprice.CourseTemplatePriceMapper;
import com.summitsync.api.qualification.QualificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseTemplateMappingService {

    private final CourseTemplatePriceMapper courseTemplatePriceMapper;
    private final QualificationMapper qualificationMapper;

    public CourseTemplate mapPostCourseTemplateDtoToCourseTemplate(PostCourseTemplateDto dto){
        return new CourseTemplate(
                dto.getAcronym(),
                dto.getTitle(),
                dto.getNumberOfDates(),
                dto.getDescription(),
                new HashSet<>(),
                dto.getNumberOfParticipants(),
                dto.getNumberOfWaitList(),
                dto.getNumberOfTrainers(),
                new HashSet<>(),
                dto.getDuration(),
                dto.getNumberOfMinutesPerDate()
        );
    }

    public CourseTemplateDto mapCourseTemplateToCourseTemplateDto(CourseTemplate courseTemplate){
        var priceListSet = courseTemplate.getPriceList()
                .stream()
                .map(this.courseTemplatePriceMapper::mapCourseTemplatePriceToCourseTemplatePriceDto)
                .collect(Collectors.toSet());

        var qualificationSet = courseTemplate.getRequiredQualifications()
                .stream()
                .map(this.qualificationMapper::mapQualificationToQualificationDto)
                .collect(Collectors.toSet());

        return CourseTemplateDto.builder()
                .id(courseTemplate.getBaseTemplateId())
                .title(courseTemplate.getTitle())
                .numberOfParticipants(courseTemplate.getNumberOfParticipants())
                .numberOfTrainers(courseTemplate.getNumberOfTrainers())
                .numberOfWaitList(courseTemplate.getNumberOfWaitList())
                .priceList(priceListSet)
                .acronym(courseTemplate.getAcronym())
                .description(courseTemplate.getDescription())
                .numberOfDates(courseTemplate.getNumberOfDates())
                .qualificationList(qualificationSet)
                .build();
    }
}
