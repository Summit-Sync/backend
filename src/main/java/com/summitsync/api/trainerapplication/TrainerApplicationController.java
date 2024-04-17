package com.summitsync.api.trainerapplication;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseMapper;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.GroupMapper;
import com.summitsync.api.group.GroupService;
import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerService;
import com.summitsync.api.trainerapplication.dto.TrainerApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer/application")
@RequiredArgsConstructor
public class TrainerApplicationController {

    private final TrainerService trainerService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TrainerApplicationService service;
    private final TrainerApplicationMapper mapper;
    private final CourseMapper courseMapper;
    private final GroupMapper groupMapper;

    @PutMapping("/{trainerId}/group/{groupId}")
    public ResponseEntity<TrainerApplicationDto> applyToGroup(@PathVariable long trainerId, @PathVariable long groupId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Group group = this.groupService.get(groupId);
        TrainerApplication application = this.service.applyToGroup(trainer, group);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }

    @PutMapping("/{trainerId}/course/{courseId}")
    public ResponseEntity<TrainerApplicationDto> applyToCourse(@PathVariable long trainerId, @PathVariable long courseId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Course course = this.courseService.get(courseId);
        TrainerApplication application = this.service.applyToCourse(trainer, course);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }

    @GetMapping("/{trainerId}/course")
    public ResponseEntity<List<CourseGetDTO>> getAppliedCourses(@PathVariable long trainerId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        List<TrainerApplication> applications = this.service.getCourseApplications(trainer);
        List<CourseGetDTO> response = new ArrayList<>();
        for (TrainerApplication application : applications) {
            response.add(this.courseMapper.mapCourseToCourseGetDTO(application.getCourse(), jwt.getToken().getTokenValue()));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{trainerId}/group")
    public ResponseEntity<List<GroupGetDTO>> getAppliedGroups(@PathVariable long trainerId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        List<TrainerApplication> applications = this.service.getGroupApplications(trainer);
        List<GroupGetDTO> response = new ArrayList<>();
        for (TrainerApplication application : applications) {
            response.add(this.groupMapper.mapGroupToGroupGetDto(application.getGroup(), jwt.getToken().getTokenValue()));
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/{trainerId}/group/{groupId}/reject")
    public ResponseEntity<TrainerApplicationDto> rejectGroupApplication(@PathVariable long trainerId, @PathVariable long groupId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Group group = this.groupService.get(groupId);
        TrainerApplication application = this.service.rejectGroupApplication(trainer, group);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }

    @PutMapping("/admin/{trainerId}/course/{courseId}/reject")
    public ResponseEntity<TrainerApplicationDto> rejectCourseApplication(@PathVariable long trainerId, @PathVariable long courseId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Course course = this.courseService.get(courseId);
        TrainerApplication application = this.service.rejectCourseApplication(trainer, course);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }

    @PutMapping("/admin/{trainerId}/group/{groupId}/approve")
    public ResponseEntity<TrainerApplicationDto> approveGroupApplication(@PathVariable long trainerId, @PathVariable long groupId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Group group = this.groupService.get(groupId);
        TrainerApplication application = this.service.acceptGroupApplication(trainer, group);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }

    @PutMapping("/admin/{trainerId}/course/{courseId}/approve")
    public ResponseEntity<TrainerApplicationDto> approveCourseApplication(@PathVariable long trainerId, @PathVariable long courseId, JwtAuthenticationToken jwt) {
        Trainer trainer = this.trainerService.findById(trainerId);
        Course course = this.courseService.get(courseId);
        TrainerApplication application = this.service.acceptCourseApplication(trainer, course);
        return ResponseEntity.ok(this.mapper.mapTrainerApplicationToTrainerApplicationDto(application, jwt));
    }
}
