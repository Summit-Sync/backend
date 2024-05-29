package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.price.Price;
import com.summitsync.api.price.PriceMapper;
import com.summitsync.api.price.PriceService;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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
        var courseTemplate = CourseTemplate.builder()
                .acronym(dto.getAcronym())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .numberOfDates(dto.getNumberOfDates())
                .duration(dto.getDuration())
                .numberParticipants(dto.getNumberParticipants())
                .numberWaitlist(dto.getNumberWaitlist())
                .meetingPoint(dto.getMeetingPoint())
                .numberTrainer(dto.getNumberTrainers())
                .build();

        if (dto.getLocation() != null) {
            courseTemplate.setLocation(this.locationService.getLocationById(dto.getLocation()));
        }

        if (dto.getPrice() != null && !dto.getPrice().isEmpty()) {
            var prices = new ArrayList<Price>();
            for (var pricePostDto: dto.getPrice()) {
                var price = Price
                        .builder()
                        .name(pricePostDto.getName())
                        .price(pricePostDto.getPrice())
                        .build();
                prices.add(this.priceService.create(price));
            }

            courseTemplate.setPrices(prices);
        }

        if (dto.getRequiredQualifications() != null && !dto.getRequiredQualifications().isEmpty()) {
            courseTemplate.setQualifications(dto.getRequiredQualifications().stream().map(this.qualificationService::findById).toList());
        }

        return courseTemplate;
    }

    public CourseTemplateDto mapCourseTemplateToCourseTemplateDto(CourseTemplate courseTemplate){
        var dto = CourseTemplateDto.builder()
                .id(courseTemplate.getCourseTemplateId())
                .acronym(courseTemplate.getAcronym())
                .title(courseTemplate.getTitle())
                .description(courseTemplate.getDescription())
                .numberOfDates(courseTemplate.getNumberOfDates())
                .duration(courseTemplate.getDuration())
                .numberParticipants(courseTemplate.getNumberParticipants())
                .numberWaitlist(courseTemplate.getNumberWaitlist())
                .meetingPoint(courseTemplate.getMeetingPoint())
                .numberTrainers(courseTemplate.getNumberTrainer())
                .build();

        if (courseTemplate.getLocation() != null) {
            dto.setLocation(this.locationMapper.mapLocationToGetLocationDto(courseTemplate.getLocation()));
        }

        if (courseTemplate.getPrices() != null && !courseTemplate.getPrices().isEmpty()) {
            dto.setPrice(courseTemplate.getPrices().stream().map(this.priceMapper::mapPriceToPriceDto).toList());
        }

        if (courseTemplate.getQualifications() != null && !courseTemplate.getQualifications().isEmpty()) {
            dto.setRequiredQualifications(courseTemplate.getQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).toList());
        }

        return dto;
    }
}
