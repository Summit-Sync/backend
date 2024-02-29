package com.summitsync.api.coursetemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coursetemplate")
public class CourseTemplateController {

    @PostMapping
    public ResponseEntity<CourseTemplateDto>createCourseTemplate(){
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
