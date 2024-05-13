package com.summitsync.api.price.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PriceDto {
    long id;
    BigDecimal price;
    String name;
}
