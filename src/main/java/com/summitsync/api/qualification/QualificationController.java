package com.summitsync.api.qualification;

import com.summitsync.api.course.CourseService;
import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.dto.AddQualificationDto;
import com.summitsync.api.qualification.dto.QualificationDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/qualification")
public class QualificationController {
    private final QualificationMapper qualificationMapper;
    private final QualificationService qualificationService;
    private final CourseService courseService;

    public QualificationController(QualificationMapper qualificationMapper, QualificationService qualificationService, CourseService courseService) {
        this.qualificationMapper = qualificationMapper;
        this.qualificationService = qualificationService;
        this.courseService = courseService;
    }
    @PostMapping
    public QualificationDto newQualification(@RequestBody @Valid AddQualificationDto addQualificationDto) {
        var qualification = this.qualificationMapper.mapAddQualificationDtoToQualification(addQualificationDto);
        var savedQualification = this.qualificationService.saveQualification(qualification);
        return this.qualificationMapper.mapQualificationToQualificationDto(savedQualification);
    }

    @GetMapping("/{id}")
    public QualificationDto getQualification(@PathVariable("id") long id) {
        var qualification = this.qualificationService.findById(id);
        return this.qualificationMapper.mapQualificationToQualificationDto(qualification);
    }

    @GetMapping()
    public List<QualificationDto> getAllQualifications() {
        var qualifications = this.qualificationService.getAllQualifications();
        return qualifications.stream().map(this.qualificationMapper::mapQualificationToQualificationDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQualification(@PathVariable("id") long id) {
        this.qualificationService.deleteQualificationById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualificationDto> putQualification(@PathVariable("id") long id, @RequestBody @Valid AddQualificationDto addQualificationDto) {
        var qualification = this.qualificationMapper.mapAddQualificationDtoToQualification(addQualificationDto);
        var ret = this.qualificationService.updateQualification(id, qualification);

        return new ResponseEntity<>(this.qualificationMapper.mapQualificationToQualificationDto(ret), HttpStatus.OK);
    }

    @GetMapping("/{id}/course")
    public ResponseEntity<List<CourseGetDTO>> getCoursesWithQualification(@PathVariable("id") long id, JwtAuthenticationToken jwt) {
        var qualification = this.qualificationService.findById(id);
        return new ResponseEntity<>(this.courseService.getAllWithQualification(qualification, jwt), HttpStatus.OK);
    }
}
