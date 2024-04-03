package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    private final CourseMapper mapper;
    private final QualificationService qualificationService;
    @PostMapping
    public ResponseEntity<CourseGetDTO> createCourseFromTemplate(@RequestBody CreateCourseWrapperDTO wrapper, JwtAuthenticationToken jwt) {
        Course course = this.service.createFromTemplate(wrapper.getTemplateId(), this.mapper.mapCoursePostDTOToCourse(wrapper.getCourse()));
        return new ResponseEntity<>(this.mapper.mapCourseToCourseGetDTO(course, jwt.getToken().getTokenValue()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseGetDTO> getCourseById(@PathVariable long id, JwtAuthenticationToken jwt) {
        Course course = this.service.get(id);
        CourseGetDTO response = this.mapper.mapCourseToCourseGetDTO(course, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CourseGetDTO>> getAllCourses(JwtAuthenticationToken jwt) {
        List<Course> all = this.service.getAll();
        List<CourseGetDTO> DTOs = new ArrayList<>();
        for (Course course : all) {
            DTOs.add(this.mapper.mapCourseToCourseGetDTO(course, jwt.getToken().getTokenValue()));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseGetDTO> deleteById(@PathVariable long id, JwtAuthenticationToken jwt) {
        Course course = this.service.deleteById(id);
        CourseGetDTO dto = this.mapper.mapCourseToCourseGetDTO(course, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseGetDTO> updateCourse(@RequestBody CoursePostDTO dto, @PathVariable long id, JwtAuthenticationToken jwt) {
        Course courseToUpdate = this.mapper.mapCoursePostDTOToCourse(dto);
        Course dbCourse = this.service.update(courseToUpdate, id);
        CourseGetDTO response = this.mapper.mapCourseToCourseGetDTO(dbCourse, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/qualification/{qualificationId}")
    public ResponseEntity<CourseGetDTO> addQualificationToCourse(@PathVariable long id, @PathVariable long qualificationId, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);
        var qualification = this.qualificationService.findById(qualificationId);

        var updatedCourse = this.service.addQualificationToCourse(course, qualification);

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));

    }

    @DeleteMapping("/{id}/qualification/{qualificationId}")
    public ResponseEntity<CourseGetDTO> removeQualificationFromCourse(@PathVariable long id, @PathVariable long qualificationId, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);
        var qualification = this.qualificationService.findById(qualificationId);

        var updatedCourse = this.service.removeQualificationFromCourse(course, qualification);

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

}
