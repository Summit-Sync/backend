package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.UpdateCourseTemplateDto;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseTemplateMappingService {

    private final QualificationService qualificationService;

    public CourseTemplate mapPostCourseTemplateDtoToCourseTemplate(PostCourseTemplateDto dto){
        List<Qualification>qualificationList=new ArrayList<>();
        for(Qualification qualification : dto.getQualificationList()){
            qualificationList.add(qualificationService.findById(qualification.getQualificationId()));
        }
        return new CourseTemplate(dto.getAcronym(), dto.getTitle(),dto.getNumberOfDates(),dto.getDescription(),qualificationList,dto.getNumberOfParticipants(), dto.getNumberOfWaitList(), dto.getNumberOfTrainers(),dto.getPriceList(),dto.getDuration(), dto.getNumberOfHoursPerDate());
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
        List<Qualification>qualificationList=new ArrayList<>();
        for(Qualification qualification : dto.getQualificationList()){
            qualificationList.add(qualificationService.findById(qualification.getQualificationId()));
        }
        return new CourseTemplate(dto.getId(),dto.getAcronym(), dto.getTitle(),dto.getNumberOfDates(),dto.getDescription(),qualificationList,dto.getNumberOfParticipants(), dto.getNumberOfWaitList(), dto.getNumberOfTrainers(),dto.getPriceList(), dto.getDuration(), dto.getNumberOfHoursPerDate());
    }

    public CourseTemplate mapGetCourseTemplateDtoToCourseTemplate(CourseTemplateDto dto) {
        List<Qualification>qualificationList=new ArrayList<>();
        for(Qualification qualification : dto.getQualificationList()){
            qualificationList.add(qualificationService.findById(qualification.getQualificationId()));
        }
        CourseTemplate newTemplate = new CourseTemplate(dto.getAcronym(), dto.getTitle(),dto.getNumberOfDates(),dto.getDescription(),qualificationList,dto.getNumberOfParticipants(), dto.getNumberOfWaitList(), dto.getNumberOfTrainers(),dto.getPriceList(),dto.getDuration(), dto.getNumberOfHoursPerDate());
        newTemplate.setBaseTemplateId(dto.getId());
        return newTemplate;
    }
}
