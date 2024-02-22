package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coursetemplate")
@RequiredArgsConstructor
public class CourseTemplateController {

    private final CourseTemplateService service;
    private final CourseTemplateMappingService courseTemplateMappingService;

    @PostMapping
    public ResponseEntity<CourseTemplateDto>createCourseTemplate(@RequestBody PostCourseTemplateDto dto){
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = checkValidity(dto);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate template=courseTemplateMappingService.mapPostCourseTemplateDtoToCourseTemplate(dto);
        CourseTemplate data=service.createCourse(template);
        CourseTemplateDto createdCourse=courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(createdCourse,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CourseTemplateDto>updateCourseTemplate(@RequestBody PostCourseTemplateDto dto){
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = checkValidity(dto);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate template=courseTemplateMappingService.mapPostCourseTemplateDtoToCourseTemplate(dto);
        CourseTemplate data=service.updateCourse(template);
        CourseTemplateDto updatedCourse=courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(updatedCourse,HttpStatus.OK);
    }

    private ResponseEntity<CourseTemplateDto> checkValidity(PostCourseTemplateDto dto) {
        if(dto.getAcronym()==null||dto.getAcronym().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getTitle()==null||dto.getTitle().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfParticipants()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfDates()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getNumberOfTrainers()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getPriceList().size()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(dto.getDuration()==0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
