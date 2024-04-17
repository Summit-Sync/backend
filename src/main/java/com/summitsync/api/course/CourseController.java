package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.participant.ParticipantService;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    private final CourseMapper mapper;
    private final QualificationService qualificationService;
    private final ParticipantService participantService;
    private final TrainerService trainerService;

    @PostMapping
    public ResponseEntity<CourseGetDTO> addCourse(@RequestBody CoursePostDTO course, JwtAuthenticationToken jwt) {
        var createdCourse = this.service.create(this.mapper.mapCoursePostDTOToCourse(course));

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(createdCourse, jwt.getToken().getTokenValue()));
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
        Course updatedCourse = this.mapper.mapCoursePostDTOToCourse(dto);
        Course dbCourse = this.service.update(this.service.get(id), updatedCourse);
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

    @PutMapping("/{id}/participant")
    public ResponseEntity<CourseGetDTO> addParticipantsToCourse(@PathVariable long id, @RequestBody Set<Long> participantIds, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);

        var updatedCourse = this.service.addParticipants(course, participantIds.stream().map(this.participantService::findById).collect(Collectors.toSet()));

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

    @DeleteMapping("/{id}/participant/{participantId}")
    public ResponseEntity<CourseGetDTO> deleteParticipantFromCourse(@PathVariable long id, @PathVariable long participantId, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);
        var updatedCourse = this.service.removeParticipants(course, participantId);

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

    @PutMapping("/{id}/waitlist")
    public ResponseEntity<CourseGetDTO> addWaitlistToCourse(@PathVariable long id, @RequestBody Set<Long> participantIds, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);

        var updatedCourse = this.service.addWaitlist(course, participantIds.stream().map(this.participantService::findById).collect(Collectors.toSet()));

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

    @DeleteMapping("/{id}/waitlist/{waitlistId}")
    public ResponseEntity<CourseGetDTO> deleteWaitlistFromCourse(@PathVariable long id, @PathVariable long waitlistId, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);
        var updatedCourse = this.service.removeWaitlist(course, waitlistId);

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

    @PutMapping("/{id}/trainer")
    public ResponseEntity<CourseGetDTO> addTrainersToWaitlist(@PathVariable long id, @RequestBody Set<Long> trainerIds, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);

        var updatedCourse = this.service.addTrainer(course, trainerIds.stream().map(this.trainerService::findById).collect(Collectors.toSet()));

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }

    @DeleteMapping("/{id}/waitlist/{trainerId}")
    public ResponseEntity<CourseGetDTO> removeTrainerFromCourse(@PathVariable long id, @PathVariable long trainerId, JwtAuthenticationToken jwt) {
        var course = this.service.get(id);
        var updatedCourse = this.service.removeTrainer(course, trainerId);

        return ResponseEntity.ok(this.mapper.mapCourseToCourseGetDTO(updatedCourse, jwt.getToken().getTokenValue()));
    }
}
