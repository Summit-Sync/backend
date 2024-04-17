package com.summitsync.api.price;

import com.summitsync.api.price.dto.PriceDto;
import com.summitsync.api.price.dto.PricePostDto;
import org.springframework.stereotype.Service;

@Service
public class PriceMapper {
    public PriceDto mapPriceToPriceDto(Price price) {
        return PriceDto.builder()
                .id(price.getCourseTemplatePriceId())
                .price(price.getPrice())
                .category(price.getName())
                .build();
    }

    public Price mapPostPriceDtoToPrice(PricePostDto pricePostDto) {
        return Price.builder()
                .name(pricePostDto.getName())
                .price(pricePostDto.getPrice())
                .build();
    }

}
