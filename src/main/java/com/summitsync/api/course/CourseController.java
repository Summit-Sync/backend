package com.summitsync.api.course;

import com.summitsync.api.coursetemplate.CourseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/course")
public class CourseController {
    private final CourseRepository repository;
    private final CourseMapper mapper;

    @Autowired
    public CourseController(CourseRepository repository) {
        this.repository = repository;
        this.mapper = new CourseMapper();
    }

    @PostMapping
    private ResponseEntity<CourseDTO> createCourseFromTemplate(@RequestBody CourseTemplate template, CourseDTO dto) {
        dto.setTemplate(template);
        Course course = mapper.mapCourseDTOToCourse(dto);
        repository.save(course);
        return new ResponseEntity<>(mapper.mapCourseToCourseDTO(course), HttpStatus.OK);
    }
}
