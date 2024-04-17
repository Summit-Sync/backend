package com.summitsync.api.price;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;
    public Price findById(long id) {
        var courseTemplatePrice = this.priceRepository.findById(id);

        if (courseTemplatePrice.isEmpty()) {
            throw new ResourceNotFoundException("CourseTemplatePrice on id " + id + " not found");
        }

        return courseTemplatePrice.get();
    }

    public Price create(Price price) {
        return this.priceRepository.save(price);
    }

    public List<Price> findAll() {
        return this.priceRepository.findAll();
    }

    public Price update(Price priceToUpdate, Price updatedPrice) {
        priceToUpdate.setName(updatedPrice.getName());
        priceToUpdate.setPrice(updatedPrice.getPrice());

        return this.priceRepository.save(priceToUpdate);
    }

    public void deleteById(long id) {
        this.priceRepository.deleteById(id);
    }
}
