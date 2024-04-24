package com.summitsync.api.price.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricePostDto {
    @NotBlank(message = "price needs to have a name")
    String name;
    @Digits(fraction = 2, integer = 6, message = "price can only have up to 2 fractional digits")
    BigDecimal price;
}
