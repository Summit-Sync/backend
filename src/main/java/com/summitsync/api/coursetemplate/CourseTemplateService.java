package com.summitsync.api.coursetemplate;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CourseTemplateService {

    private final Logger log= LoggerFactory.getLogger(CourseTemplateService.class);
    private final CourseTemplateRepository repository;

    public CourseTemplate createCourse(CourseTemplate newTemplate){
        return repository.save(newTemplate);
    }
}
