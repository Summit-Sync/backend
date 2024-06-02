package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final MailService mailService;

    public Course create(Course course) {
        course.setCourseNumber(this.generateCourseNumber(course.getAcronym()));
        return this.repository.save(course);
    }

    private String generateCourseNumber(String acronym) {
        var courses = this.repository.findByAcronymOrderByCourseNumberDesc(acronym);
        int ret = courses.isEmpty() ? 1 : Integer.parseInt(courses.getFirst().getCourseNumber()) + 1;
        return String.format("%03d", ret);
    }

    public Course update(Course courseToUpdate, Course course, boolean cancelled, boolean finished, String jwt) {
        List<EventDate> originalDates = courseToUpdate.getDates();
        List<EventDate> newDates = course.getDates();

        boolean dateChanged = false;

        if (originalDates.size() != newDates.size()) {
            dateChanged = true;
        } else {
            Map<Long, EventDate> originalDatesMap = originalDates.stream()
                    .collect(Collectors.toMap(EventDate::getEventDateId, Function.identity()));

            for (EventDate newDate : newDates) {
                EventDate originalDate = originalDatesMap.get(newDate.getEventDateId());
                if (originalDate == null || !newDate.getStartTime().equals(originalDate.getStartTime()) ||
                        newDate.getDurationInMinutes() != originalDate.getDurationInMinutes()) {
                    dateChanged = true;
                    break;
                }
            }
        }

        if (dateChanged) {
            mailService.sendCourseChangeMail(course, jwt);
        }

        var participants = new ArrayList<>(course.getParticipants());
        courseToUpdate.setParticipants(participants);
        var trainers = new ArrayList<>(course.getTrainers());
        courseToUpdate.setTrainers(trainers);
        var waitList = new ArrayList<>(course.getWaitList());
        courseToUpdate.setWaitList(waitList);
        courseToUpdate.setVisible(course.isVisible());
        courseToUpdate.setCancelled(course.isCancelled());
        courseToUpdate.setFinished(course.isFinished());
        courseToUpdate.setAcronym(course.getAcronym());
        //TODO only update removed and new dates
        var dates = new ArrayList<>(course.getDates());
        courseToUpdate.setDates(dates);
        courseToUpdate.setDuration(course.getDuration());
        courseToUpdate.setNumberParticipants(course.getNumberParticipants());
        courseToUpdate.setNumberWaitlist(course.getNumberWaitlist());
        courseToUpdate.setCoursePrices(course.getCoursePrices());
        courseToUpdate.setLocation(course.getLocation());
        courseToUpdate.setMeetingPoint(course.getMeetingPoint());
        var qualifications = new ArrayList<>(course.getRequiredQualifications());
        courseToUpdate.setRequiredQualifications(qualifications);
        courseToUpdate.setNumberTrainer(course.getNumberTrainer());
        courseToUpdate.setNotes(course.getNotes());
        courseToUpdate.setTitle(course.getTitle());
        courseToUpdate.setCancelled(cancelled);
        courseToUpdate.setFinished(finished);
        courseToUpdate.setDescription(course.getDescription());
        if (!courseToUpdate.getAcronym().equals(course.getAcronym())) {
            courseToUpdate.setCourseNumber(this.generateCourseNumber(courseToUpdate.getAcronym()));
        }
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
                .toList();

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
                .toList();

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
                .toList();

        course.setParticipants(updatedParticipants);
        return this.repository.save(course);
    }

    public Course addTrainer(Course course, Set<Trainer> trainers) {
        var oldTrainers = course.getTrainers();
        oldTrainers.addAll(trainers);
        course.setTrainers(oldTrainers);
        return this.repository.save(course);
    }

    public Course removeTrainer(Course course, Long trainerId) {
        var trainers = course.getTrainers();

        var updatedTrainers = trainers
                .stream()
                .filter(
                        t -> t.getTrainerId() != trainerId
                )
                .toList();

        course.setTrainers(updatedTrainers);
        return this.repository.save(course);
    }

    public List<CourseGetDTO> getAllWithQualification(Qualification qualification, JwtAuthenticationToken jwt) {
        var all = this.getAll();
        var filter = all.stream().filter(course -> course.getRequiredQualifications().contains(qualification));
        var courses = filter.toList();
        List<CourseGetDTO> result = new ArrayList<>();
        for (Course course : courses) {
            result.add(this.mapper.mapCourseToCourseGetDTO(course, jwt.getToken().getTokenValue()));
        }
        return result;
    }

    public Course cancel(Course course, boolean canceling, String jwt) {
        course.setCancelled(canceling);
        Course canceledCourse=this.repository.save(course);
        mailService.sendCourseCancelMail(canceledCourse, jwt);
        return canceledCourse;
    }

    public Course publish(Course course, boolean published) {
        course.setVisible(published);
        return this.repository.save(course);
    }
}
