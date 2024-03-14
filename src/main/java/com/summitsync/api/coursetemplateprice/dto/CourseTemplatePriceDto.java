package com.summitsync.api.coursetemplateprice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CourseTemplatePriceDto {
    long id;
    BigDecimal price;
    String category;
}
