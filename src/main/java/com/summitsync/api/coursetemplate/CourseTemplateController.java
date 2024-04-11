package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.coursetemplateprice.CourseTemplatePriceService;
import com.summitsync.api.qualification.QualificationService;
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

    private final CourseTemplateService service;
    private final CourseTemplateMappingService courseTemplateMappingService;
    private final QualificationService qualificationService;
    private final CourseTemplatePriceService courseTemplatePriceService;

    @PostMapping
    public ResponseEntity<CourseTemplateDto>createCourseTemplate(@RequestBody PostCourseTemplateDto dto){
        CourseTemplate template = this.courseTemplateMappingService.mapPostCourseTemplateDtoToCourseTemplate(dto);
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = this.checkValidity(template);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate data = this.service.createCourse(template);
        CourseTemplateDto createdCourse = this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(createdCourse,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseTemplateDto>updateCourseTemplate(@RequestBody PostCourseTemplateDto dto, @PathVariable final Long id){
        CourseTemplate template = this.courseTemplateMappingService.mapPostCourseTemplateDtoToCourseTemplate(dto);
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = this.checkValidity(template);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate data = this.service.updateCourse(template,id);
        CourseTemplateDto updatedCourse = this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(updatedCourse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCourseTemplate(@PathVariable Long id){
        this.service.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<CourseTemplateDto>> getAllCourseTemplates(){
        List<CourseTemplate>data = this.service.findAll();
        List<CourseTemplateDto>response = new ArrayList<>();
        for(CourseTemplate courseTemplate : data){

            response.add(this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(courseTemplate));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseTemplateDto> getCourseTemplate(@PathVariable long id) {
        CourseTemplate template = this.service.findById(id);
        return new ResponseEntity<>(this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(template), HttpStatus.OK);
    }

    @PostMapping("/{courseTemplateId}/qualification/{qualificationId}")
    public CourseTemplateDto addQualificationToCourseTemplate(@PathVariable long courseTemplateId, @PathVariable long qualificationId) {
        var courseTemplate = this.service.findById(courseTemplateId);
        var qualification = this.qualificationService.findById(qualificationId);
        var updatedCourseTemplate = this.service.addQualificationToCourseTemplate(courseTemplate, qualification);

        return this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(updatedCourseTemplate);
    }

    @DeleteMapping("/{courseTemplateId}/qualification/{qualificationId}")
    public CourseTemplateDto deleteQualificationFromCourseTemplate(@PathVariable long courseTemplateId, @PathVariable long qualificationId) {
        var courseTemplate = this.service.findById(courseTemplateId);
        var qualification = this.qualificationService.findById(qualificationId);
        var updatedCourseTemplate = this.service.removeQualificationFromCourseTemplate(courseTemplate, qualification);

        return this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(updatedCourseTemplate);
    }

    @PostMapping("/{courseTemplateId}/price/{courseTemplatePriceId}")
    public CourseTemplateDto addPriceToCourseTemplate(@PathVariable long courseTemplateId, @PathVariable long courseTemplatePriceId) {
        var courseTemplate = this.service.findById(courseTemplateId);
        var courseTemplatePrice = this.courseTemplatePriceService.findById(courseTemplatePriceId);

        var updatedCourseTemplate = this.service.addPriceToCourseTemplate(courseTemplate, courseTemplatePrice);

        return this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(updatedCourseTemplate);
    }

    @DeleteMapping("/{courseTemplateId}/price/{courseTemplatePriceId}")
    public CourseTemplateDto deletePriceFromCourseTemplate(@PathVariable long courseTemplateId, @PathVariable long courseTemplatePriceId) {
        var courseTemplate = this.service.findById(courseTemplateId);
        var courseTemplatePrice = this.courseTemplatePriceService.findById(courseTemplatePriceId);

        var updatedCourseTemplate = this.service.removePriceFromCourseTemplate(courseTemplate, courseTemplatePrice);

        return this.courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(updatedCourseTemplate);
    }

    private ResponseEntity<CourseTemplateDto> checkValidity(CourseTemplate dto) {
        if(dto.getAcronym() == null || dto.getAcronym().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getTitle() == null || dto.getTitle().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfParticipants() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfDates() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfTrainers() == 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getPriceList().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getDuration() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
