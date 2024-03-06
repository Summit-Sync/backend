package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateMappingService;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.dto.GroupGetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    private final CourseMapper mapper;
    private final CourseTemplateMappingService templateMapper;

    @PostMapping
    public ResponseEntity<CourseGetDTO> createCourseFromTemplate(@RequestBody CreateCourseWrapperDTO wrapper) {
        Course course = service.createFromTemplate(templateMapper.mapGetCourseTemplateDtoToCourseTemplate(wrapper.getTemplate()), mapper.mapCourseGetDTOToCourse(wrapper.getCourse()));
        return new ResponseEntity<>(mapper.mapCourseToCourseGetDTO(course), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseGetDTO> getCourseById(@PathVariable long id) {
        Course course = service.get(id);
        CourseGetDTO response = mapper.mapCourseToCourseGetDTO(course);
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
        Course course = service.deleteById(id);
        CourseGetDTO dto = mapper.mapCourseToCourseGetDTO(course);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseGetDTO> updateCourse(@RequestBody CourseGetDTO dto, @PathVariable long id) {
        Course courseToUpdate = mapper.mapCourseGetDTOToCourse(dto);
        Course dbCourse = service.update(courseToUpdate, id);
        CourseGetDTO response = mapper.mapCourseToCourseGetDTO(dbCourse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
