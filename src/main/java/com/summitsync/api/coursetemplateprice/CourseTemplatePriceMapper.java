package com.summitsync.api.coursetemplateprice;

import com.summitsync.api.coursetemplateprice.dto.CourseTemplatePriceDto;
import org.springframework.stereotype.Service;

@Service
public class CourseTemplatePriceMapper {
    public CourseTemplatePriceDto mapCourseTemplatePriceToCourseTemplatePriceDto(CourseTemplatePrice courseTemplatePrice) {
        return CourseTemplatePriceDto.builder()
                .id(courseTemplatePrice.getCourseTemplateId())
                .price(courseTemplatePrice.getPrice())
                .category(courseTemplatePrice.getCategory())
                .build();
    }

}
