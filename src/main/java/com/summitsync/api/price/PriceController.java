package com.summitsync.api.price;

import com.summitsync.api.price.dto.PriceDto;
import com.summitsync.api.price.dto.PricePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;
    private final PriceMapper priceMapper;

    @PostMapping
    public PriceDto newPrice(@RequestBody PricePostDto pricePostDto) {
        var dbPrice = this.priceService.create(this.priceMapper.mapPostPriceDtoToPrice(pricePostDto));

        return this.priceMapper.mapPriceToPriceDto(dbPrice);
    }

    @GetMapping
    public List<PriceDto> getAllPrices() {
        return this.priceService.findAll().stream().map(this.priceMapper::mapPriceToPriceDto).toList();
    }

    @GetMapping("/{id}")
    public PriceDto getPriceById(@PathVariable long id) {
        return this.priceMapper.mapPriceToPriceDto(this.priceService.findById(id));
    }

    @PutMapping("/{id}")
    public PriceDto updatePrice(@RequestBody PricePostDto pricePostDto, @PathVariable long id) {
        var priceToUpdate = this.priceService.findById(id);
        var updatedPrice = this.priceMapper.mapPostPriceDtoToPrice(pricePostDto);
        return this.priceMapper.mapPriceToPriceDto(this.priceService.update(priceToUpdate, updatedPrice));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrice(@PathVariable long id) {
        this.priceService.deleteById(id);

    }
}
