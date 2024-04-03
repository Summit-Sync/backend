package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    private final CourseMapper mapper;
    @PostMapping
    public ResponseEntity<CourseGetDTO> createCourseFromTemplate(@RequestBody CreateCourseWrapperDTO wrapper) {
        Course course = this.service.createFromTemplate(wrapper.getTemplateId(), this.mapper.mapCourseGetDTOToCourse(wrapper.getCourse()));
        return new ResponseEntity<>(this.mapper.mapCourseToCourseGetDTO(course), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseGetDTO> getCourseById(@PathVariable long id) {
        Course course = this.service.get(id);
        CourseGetDTO response = this.mapper.mapCourseToCourseGetDTO(course);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CourseGetDTO>> getAllCourses() {
        List<Course> all = this.service.getAll();
        List<CourseGetDTO> DTOs = new ArrayList<>();
        for (Course course : all) {
            DTOs.add(this.mapper.mapCourseToCourseGetDTO(course));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseGetDTO> deleteById(@PathVariable long id) {
        Course course = this.service.deleteById(id);
        CourseGetDTO dto = this.mapper.mapCourseToCourseGetDTO(course);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseGetDTO> updateCourse(@RequestBody CoursePostDTO dto, @PathVariable long id) {
        Course courseToUpdate = this.mapper.mapCoursePostDTOToCourse(dto);
        Course dbCourse = this.service.update(courseToUpdate, id);
        CourseGetDTO response = this.mapper.mapCourseToCourseGetDTO(dbCourse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/qualification/{qualificationId}")
    public ResponseEntity<CourseGetDTO> addQualificationToCourse(@PathVariable long id, @PathVariable long qualificationId) {

    }

    @DeleteMapping("/{id}/qualification/{qualificationId}")
    public ResponseEntity<CourseGetDTO> removeQualificationFromCourse(@PathVariable long id, @PathVariable long qualificationId) {

    }

}
