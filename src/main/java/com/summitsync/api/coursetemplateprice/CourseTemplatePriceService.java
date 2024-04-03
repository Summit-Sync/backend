package com.summitsync.api.coursetemplateprice;

import com.summitsync.api.coursetemplate.CourseTemplateRepository;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseTemplatePriceService {
    private final CourseTemplatePriceRepository courseTemplateRepository;
    public CourseTemplatePrice findById(long id) {
        var courseTemplatePrice = this.courseTemplateRepository.findById(id);

        if (courseTemplatePrice.isEmpty()) {
            throw new ResourceNotFoundException("CourseTemplatePrice on id " + id + " not found");
        }

        return courseTemplatePrice.get();
    }
}
