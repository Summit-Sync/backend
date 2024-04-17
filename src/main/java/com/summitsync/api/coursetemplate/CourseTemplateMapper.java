package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.price.PriceMapper;
import com.summitsync.api.price.PriceService;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseTemplateMapper {

    private final PriceMapper priceMapper;
    private final QualificationMapper qualificationMapper;
    private final LocationService locationService;
    private final PriceService priceService;
    private final QualificationService qualificationService;
    private final LocationMapper locationMapper;

    public CourseTemplate mapPostCourseTemplateDtoToCourseTemplate(PostCourseTemplateDto dto){
        return CourseTemplate.builder()
                .acronym(dto.getAcronym())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .numberOfDates(dto.getNumberOfDates())
                .duration(dto.getDuration())
                .numberParticipants(dto.getNumberParticipants())
                .numberWaitlist(dto.getNumberWaitlist())
                .location(this.locationService.getLocationById(dto.getLocation()))
                .meetingPoint(dto.getMeetingPoint())
                .prices(dto.getPrice().stream().map(this.priceService::findById).collect(Collectors.toSet()))
                .qualifications(dto.getRequiredQualifications().stream().map(this.qualificationService::findById).collect(Collectors.toSet()))
                .numberTrainer(dto.getNumberTrainers())
                .build();

    }

    public CourseTemplateDto mapCourseTemplateToCourseTemplateDto(CourseTemplate courseTemplate){
        return CourseTemplateDto.builder()
                .id(courseTemplate.getCourseTemplateId())
                .acronym(courseTemplate.getAcronym())
                .title(courseTemplate.getTitle())
                .description(courseTemplate.getDescription())
                .numberOfDates(courseTemplate.getNumberOfDates())
                .duration(courseTemplate.getDuration())
                .numberParticipants(courseTemplate.getNumberParticipants())
                .numberWaitlist(courseTemplate.getNumberWaitlist())
                .location(this.locationMapper.mapLocationToGetLocationDto(courseTemplate.getLocation()))
                .meetingPoint(courseTemplate.getMeetingPoint())
                .price(courseTemplate.getPrices().stream().map(this.priceMapper::mapPriceToPriceDto).toList())
                .requiredQualifications(courseTemplate.getQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).toList())
                .build();
    }
}
