package com.summitsync.api.price.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricePostDto {
    @NotBlank(message = "price needs to have a name")
    String name;
    @NotNull(message = "price can not be empty")
    @Digits(fraction = 2, integer = 6, message = "price can only have up to 2 fractional digits")
    BigDecimal price;
}
