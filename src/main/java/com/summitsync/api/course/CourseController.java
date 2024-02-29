package com.summitsync.api.course;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseRepository repository;
    private final CourseMapper mapper;

    @Autowired
    public CourseController(CourseRepository repository) {
        this.repository = repository;
        this.mapper = new CourseMapper();
    }

    @PostMapping("/new")
    private ResponseEntity<CourseDTO> createCourseFromTemplate(@RequestBody CourseTemplate template, CourseDTO dto) {
        dto.setTemplate(template);
        Course course = mapper.mapCourseDTOToCourse(dto);
        repository.save(course);
        return new ResponseEntity<>(mapper.mapCourseToCourseDTO(course), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    private ResponseEntity<CourseDTO> getCourseById(@PathVariable long id) {
        Optional<Course> course = repository.findById(id);
        if (course.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.mapper.mapCourseToCourseDTO(course.get()), HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<Course> all = this.repository.findAll();
        List<CourseDTO> dtos = new ArrayList<>();
        for (Course course : all) {
            dtos.add(this.mapper.mapCourseToCourseDTO(course));
        }
        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<CourseDTO> deleteById(@PathVariable long id) {
        Optional<Course> course = repository.findById(id);
        if (course.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapCourseToCourseDTO(course.get()), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO dto, @PathVariable long id) {
        Optional<Course> course = repository.findById(id);
        if (course.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Course newCourse = mapper.mapCourseDTOToCourse(dto);
        this.repository.save(newCourse);
        return new ResponseEntity<>(this.mapper.mapCourseToCourseDTO(newCourse), HttpStatus.OK);
    }
}
