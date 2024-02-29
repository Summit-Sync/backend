package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.PostCourseTemplateDto;
import com.summitsync.api.coursetemplate.dto.UpdateCourseTemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coursetemplate")
@RequiredArgsConstructor
public class CourseTemplateController {

    private final CourseTemplateService service;
    private final CourseTemplateMappingService courseTemplateMappingService;

    @PostMapping
    public ResponseEntity<CourseTemplateDto>createCourseTemplate(@RequestBody PostCourseTemplateDto dto){
        CourseTemplate template=courseTemplateMappingService.mapPostCourseTemplateDtoToCourseTemplate(dto);
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = checkValidity(template);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate data=service.createCourse(template);
        CourseTemplateDto createdCourse=courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(createdCourse,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseTemplateDto>updateCourseTemplate(@RequestBody UpdateCourseTemplateDto dto, @PathVariable final Long id){
        CourseTemplate template=courseTemplateMappingService.mapUpdateCourseTemplateDtoToCourseTemplate(dto);
        ResponseEntity<CourseTemplateDto> BAD_REQUEST = checkValidity(template);
        if (BAD_REQUEST != null){
            return BAD_REQUEST;
        }
        CourseTemplate data=service.updateCourse(template,id);
        CourseTemplateDto updatedCourse=courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(data);
        return new ResponseEntity<>(updatedCourse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCourseTemplate(@PathVariable Long id){
        service.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<CourseTemplateDto>>getAllCourseTemplates(){
        List<CourseTemplate>data=service.findAll();
        List<CourseTemplateDto>response=new ArrayList<>();
        for(CourseTemplate courseTemplate:data){
            response.add(courseTemplateMappingService.mapCourseTemplateToCourseTemplateDto(courseTemplate));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private ResponseEntity<CourseTemplateDto> checkValidity(CourseTemplate dto) {
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
