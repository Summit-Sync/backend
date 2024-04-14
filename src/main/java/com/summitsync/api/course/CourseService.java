package com.summitsync.api.course;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateService;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository repository;

    public Course create(Course course) { return this.repository.save(course); }

    public Course update(Course courseToUpdate, Course course) {
        courseToUpdate.setVisible(course.isVisible());
        courseToUpdate.setCancelled(course.isCancelled());
        courseToUpdate.setFinished(course.isFinished());
        courseToUpdate.setCourseNumber(course.getCourseNumber());
        courseToUpdate.setAcronym(course.getAcronym());
        // FIXME: we always create new dates here...
        courseToUpdate.setDates(course.getDates());
        courseToUpdate.setDuration(course.getDuration());
        courseToUpdate.setNumberParticipants(course.getNumberParticipants());
        courseToUpdate.setNumberWaitlist(course.getNumberWaitlist());
        courseToUpdate.setCoursePrices(course.getCoursePrices());
        courseToUpdate.setLocation(course.getLocation());
        courseToUpdate.setMeetingPoint(course.getMeetingPoint());
        courseToUpdate.setRequiredQualifications(course.getRequiredQualifications());
        courseToUpdate.setNumberTrainer(course.getNumberTrainer());
        courseToUpdate.setNotes(course.getNotes());
        return this.repository.save(courseToUpdate);
    }

    public Course deleteById(long id) {
        Course course = this.findById(id);
        this.repository.deleteById(id);
        return course;
    }

    public Course get(long id) {
        return this.findById(id);
    }

    public List<Course> getAll() {
        List<Course> all = this.repository.findAll();
        if (all.isEmpty()) {
            log.info("CourseList is empty");
        }
        return all;
    }

    private Course findById(Long id) {
        var course = this.repository.findById(id);
        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course on id " +  id + " not found");
        }

        return course.get();
    }

    public Course addQualificationToCourse(Course course, Qualification qualification) {
        var qualificationList = course.getRequiredQualifications();
        qualificationList.add(qualification);

        course.setRequiredQualifications(qualificationList);

        return this.repository.save(course);
    }

    public Course removeQualificationFromCourse(Course course, Qualification qualification) {
        var qualificationList = course.getRequiredQualifications();

        var updatedQualificationList = qualificationList
                .stream()
                .filter(
                        q -> q.getQualificationId() != qualification.getQualificationId()
                )
                .collect(Collectors.toSet());

        course.setRequiredQualifications(updatedQualificationList);
        return this.repository.save(course);
    }

    public Course addParticipants(Course course, Set<Participant> participants) {
        course.getParticipants().addAll(participants);

        return this.repository.save(course);
    }

    public Course removeParticipants(Course course, Long participantId) {
        var participants = course.getParticipants();

        var updatedParticipants = participants
                .stream()
                .filter(
                        p -> p.getParticipantId() != participantId
                )
                .collect(Collectors.toSet());

        course.setParticipants(updatedParticipants);
        return this.repository.save(course);
    }

    public Course addWaitlist(Course course, Set<Participant> participants) {
        course.getWaitList().addAll(participants);

        return this.repository.save(course);
    }

    public Course removeWaitlist(Course course, Long participantId) {
        var participants = course.getWaitList();

        var updatedParticipants = participants
                .stream()
                .filter(
                        p -> p.getParticipantId() != participantId
                )
                .collect(Collectors.toSet());

        course.setParticipants(updatedParticipants);
        return this.repository.save(course);
    }

    public Course addTrainer(Course course, Set<Trainer> trainers) {
        course.getTrainers().addAll(trainers);

        return this.repository.save(course);
    }

    public Course removeTrainer(Course course, Long trainerId) {
        var trainers = course.getTrainers();

        var updatedTrainers = trainers
                .stream()
                .filter(
                        t -> t.getTrainerId() != trainerId
                )
                .collect(Collectors.toSet());

        course.setTrainers(updatedTrainers);
        return this.repository.save(course);
    }
}
