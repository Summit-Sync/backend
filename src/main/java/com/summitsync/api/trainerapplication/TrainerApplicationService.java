package com.summitsync.api.trainerapplication;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.GroupService;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TrainerApplicationService {

    private final TrainerApplicationRepository repository;
    private final GroupService groupService;
    private final CourseService courseService;
    public TrainerApplication applyToGroup(Trainer trainer, Group group) {
        TrainerApplication newApplication = new TrainerApplication();
        newApplication.setTrainer(trainer);
        newApplication.setGroup(group);
        newApplication.setAccepted(AcceptStatus.Pending);
        return this.repository.save(newApplication);
    }

    public TrainerApplication applyToCourse(Trainer trainer, Course course) {
        TrainerApplication newApplication = new TrainerApplication();
        newApplication.setTrainer(trainer);
        newApplication.setCourse(course);
        newApplication.setAccepted(AcceptStatus.Pending);
        return this.repository.save(newApplication);
    }

    public List<TrainerApplication> getCourseApplications(Trainer trainer) {
        return this.repository.findAll().stream().filter(trainerApplication -> trainerApplication.getTrainer().getTrainerId() == trainer.getTrainerId() && trainerApplication.getCourse() != null).toList();
    }

    public List<TrainerApplication> getGroupApplications(Trainer trainer) {
        return this.repository.findAll().stream().filter(trainerApplication -> trainerApplication.getTrainer().getTrainerId() == trainer.getTrainerId() && trainerApplication.getGroup() != null).toList();
    }

    public TrainerApplication rejectGroupApplication(Trainer trainer, Group group) {
        TrainerApplication application = getGroupApplications(trainer, group);
        application.setAccepted(AcceptStatus.Rejected);
        return this.repository.save(application);
    }


    public TrainerApplication rejectCourseApplication(Trainer trainer, Course course) {
        TrainerApplication application = getCourseApplications(trainer, course);
        application.setAccepted(AcceptStatus.Rejected);
        return this.repository.save(application);
    }

    public TrainerApplication acceptGroupApplication(Trainer trainer, Group group) {
        TrainerApplication application = getGroupApplications(trainer, group);
        application.setAccepted(AcceptStatus.Accepted);
        this.groupService.addTrainer(group, new HashSet<>(Collections.singletonList(trainer)));
        return this.repository.save(application);
    }

    private TrainerApplication getGroupApplications(Trainer trainer, Group group) {
        Optional<TrainerApplication> data = this.getGroupApplications(trainer).stream().filter(trainerApplication -> trainerApplication.getGroup().getGroupId() == group.getGroupId() && trainerApplication.getAccepted() == AcceptStatus.Pending).findFirst();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("Application not found");
        }
        return data.get();
    }


    public TrainerApplication acceptCourseApplication(Trainer trainer, Course course) {
        TrainerApplication application = getCourseApplications(trainer, course);
        application.setAccepted(AcceptStatus.Accepted);
        this.courseService.addTrainer(course, Set.of(trainer));
        return this.repository.save(application);
    }

    private TrainerApplication getCourseApplications(Trainer trainer, Course course) {
        Optional<TrainerApplication> data = this.getCourseApplications(trainer).stream().filter(trainerApplication -> trainerApplication.getCourse().getCourseId() == course.getCourseId() && trainerApplication.getAccepted() == AcceptStatus.Pending).findFirst();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("Application not found");
        }
        return data.get();
    }
}
