package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/template/course")
@RequiredArgsConstructor
public class CourseTemplateController {

    private final CourseTemplateService courseTemplateService;
    private final CourseTemplateMapper courseTemplateMapper;

    @PostMapping
    public ResponseEntity<CourseTemplateDto>createCourseTemplate(@RequestBody PostCourseTemplateDto dto){
        CourseTemplate template = this.courseTemplateMapper.mapPostCourseTemplateDtoToCourseTemplate(dto);
        CourseTemplate data = this.courseTemplateService.createCourse(template);
        CourseTemplateDto createdCourse = this.courseTemplateMapper.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(createdCourse,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseTemplateDto>updateCourseTemplate(@RequestBody PostCourseTemplateDto dto, @PathVariable final Long id){
        CourseTemplate updatedCourse = this.courseTemplateMapper.mapPostCourseTemplateDtoToCourseTemplate(dto);
        var courseToUpdate = this.courseTemplateService.findById(id);
        CourseTemplate data = this.courseTemplateService.updateCourse(courseToUpdate, updatedCourse);
        return new ResponseEntity<>(this.courseTemplateMapper.mapCourseTemplateToCourseTemplateDto(data),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCourseTemplate(@PathVariable Long id){
        this.courseTemplateService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<CourseTemplateDto>>getAllCourseTemplates(){
        List<CourseTemplate>data = this.courseTemplateService.findAll();
        List<CourseTemplateDto>response = new ArrayList<>();
        for(CourseTemplate courseTemplate : data){
            response.add(this.courseTemplateMapper.mapCourseTemplateToCourseTemplateDto(courseTemplate));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
