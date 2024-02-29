package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.UpdateCourseTemplateDto;
import com.summitsync.api.qualification.Qualification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseTemplateMappingService {

    public CourseTemplate mapPostCourseTemplateDtoToCourseTemplate(PostCourseTemplateDto dto){
        return new CourseTemplate(dto.getAcronym(), dto.getTitle(),dto.getNumberOfDates(),dto.getDescription(),dto.getQualificationList(),dto.getNumberOfParticipants(), dto.getNumberOfWaitList(), dto.getNumberOfTrainers(),dto.getPriceList(),dto.getDuration());
    }

    public CourseTemplateDto mapCourseTemplateToCourseTemplateDto(CourseTemplate data){
        return CourseTemplateDto.builder()
                .id(data.getBaseTemplateId())
                .title(data.getTitle())
                .numberOfParticipants(data.getNumberOfParticipants())
                .numberOfTrainers(data.getNumberOfTrainers())
                .numberOfWaitList(data.getNumberOfWaitList())
                .priceList(data.getPriceList())
                .acronym(data.getAcronym())
                .description(data.getDescription())
                .numberOfDates(data.getNumberOfDates())
                .qualificationList(data.getRequiredQualifications())
                .build();
    }

    public CourseTemplate mapUpdateCourseTemplateDtoToCourseTemplate(UpdateCourseTemplateDto dto){
        return new CourseTemplate(dto.getId(),dto.getAcronym(), dto.getTitle(),dto.getNumberOfDates(),dto.getDescription(),dto.getQualificationList(),dto.getNumberOfParticipants(), dto.getNumberOfWaitList(), dto.getNumberOfTrainers(),dto.getPriceList(), dto.getDuration());
    }

}
