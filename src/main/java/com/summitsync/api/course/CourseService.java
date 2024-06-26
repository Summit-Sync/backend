package com.summitsync.api.course;

import com.summitsync.api.calendar.CalendarService;
import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final MailService mailService;
    private final EventDateService eventDateService;
    private final QualificationService qualificationService;
    private final CalendarService calendarService;
    private final KeycloakRestService keycloakRestService;

    @Transactional
    public Course create(Course course) {
        course.setCourseNumber(this.generateCourseNumber(course.getAcronym()));
        List<EventDate>eventDateList=course.getDates();
        course.setDates(new ArrayList<>());
        for(EventDate e: eventDateList){
            EventDate eventDate = new EventDate();
            eventDate.setDurationInMinutes(e.getDurationInMinutes());
            eventDate.setStartTime(e.getStartTime());
            var dbDate = this.eventDateService.create(eventDate);
            course.getDates().add(dbDate);
        }
        List<Qualification>qualificationList=course.getRequiredQualifications();
        course.setRequiredQualifications(new ArrayList<>());
        for(Qualification q: qualificationList){
            course.getRequiredQualifications().add(qualificationService.findById(q.getQualificationId()));
        }

        var dbCourse = this.repository.save(course);

        try {
            dbCourse.createEvents(this.calendarService, this.keycloakRestService);
        } catch (IOException e) {
            log.warn("Failed to create calendar events for course {}", course.getAcronym(), e);
        }
        return dbCourse;
    }

    private String generateCourseNumber(String acronym) {
        var courses = this.repository.findByAcronymOrderByCourseNumberDesc(acronym);
        int ret = courses.isEmpty() ? 1 : Integer.parseInt(courses.getFirst().getCourseNumber()) + 1;
        return String.format("%03d", ret);
    }

    public Course update(Course courseToUpdate, Course course, boolean cancelled, boolean finished, String jwt) {
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

        try {
            courseToUpdate.occurrencesModification(
                    CourseMapper.mapEventDatesListToCalendarEventDateSetList(courseToUpdate.getDates()),
                    CourseMapper.mapEventDatesListToCalendarEventDateSetList(course.getDates()),
                    this.calendarService,
                    this.keycloakRestService
            );
        } catch (IOException e) {
            log.warn("Failed to modify calendar events for group {}", course.getAcronym(), e);
        }

        boolean updatedDateList=updateDatesList(courseToUpdate.getDates(),course.getDates());
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
        courseToUpdate = this.repository.save(courseToUpdate);

        if(updatedDateList){
            mailService.sendCourseChangeMail(courseToUpdate, jwt);
        }

        try {
            courseToUpdate.updateEvents(calendarService, keycloakRestService);
        } catch (IOException e) {
            log.warn("Failed to update calendar events for course {}", courseToUpdate.getAcronym(), e);
        }

        return courseToUpdate;
    }

    public Course deleteById(long id) {
        Course course = this.findById(id);
        try {
            course.deleteEvents(this.calendarService);
        } catch (IOException e) {
            log.warn("Failed to delete calendar events for course {}", course.getAcronym(), e);
        }
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

    public Course cancel(Course course, String jwt) {
        course.setCancelled(!course.isCancelled());
        Course canceledCourse=this.repository.save(course);
        mailService.sendCourseCancelMail(canceledCourse, jwt);
        return canceledCourse;
    }

    public Course publish(Course course, boolean published) {
        course.setVisible(published);
        return this.repository.save(course);
    }

    public List<Course>getAllCoursesWithMissingTrainer(){
        return this.repository.findAllWithMissingTrainers();
    }

    public boolean updateDatesList(List<EventDate>oldDates, List<EventDate>newDates){
        boolean removedDates=removeUnusedOrUpdatedDates(oldDates, newDates);
        boolean addedNewDates=addNewDates(oldDates,newDates);
        return removedDates||addedNewDates;
    }

    private boolean addNewDates(List<EventDate> oldDates, List<EventDate>newDates){
        return oldDates.addAll(newDates);
    }

    public boolean removeUnusedOrUpdatedDates(List<EventDate> oldDates, List<EventDate>newDates){
        boolean removedOldDate = false;
        Iterator<EventDate>iterator=oldDates.iterator();
        while (iterator.hasNext()){
            EventDate oldDate = iterator.next();
            if(checkIfDateIsUnusedOrUpdated(oldDate, newDates)){
                removedOldDate=true;
                iterator.remove();
            }
        }
        return removedOldDate;
    }

    private boolean checkIfDateIsUnusedOrUpdated(EventDate oldDate, List<EventDate>newDates){
        for(EventDate newDate: newDates){
            // Check if oldDate has same startTime as newDate in list of newDates
            // and the duration of the new date is the same as in the old date
            if(newDate.getStartTime().isEqual(oldDate.getStartTime()) && newDate.getDurationInMinutes() == oldDate.getDurationInMinutes()){
                // If oldDate equals date in newDates, remove the element from newDates
                newDates.remove(newDate);
                return false;
            }
        }
        return true;
    }
}
