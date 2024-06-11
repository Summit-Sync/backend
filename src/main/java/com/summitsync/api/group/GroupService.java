package com.summitsync.api.group;

import com.summitsync.api.calendar.CalendarService;
import com.summitsync.api.course.CourseMapper;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.grouptemplate.GroupTemplateService;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final MailService mailService;
    private final Logger log = LoggerFactory.getLogger(GroupService.class);
    private final CalendarService calendarService;
    private final QualificationService qualificationService;
    private final KeycloakRestService keycloakRestService;
    private final EventDateService eventDateService;

    @Transactional
    public Group create(Group group) {
        group.setGroupNumber(generateGroupNumber(group.getAcronym()));

        List<Qualification>qualificationList=group.getQualifications();
        group.setQualifications(new ArrayList<>());
        for(Qualification q: qualificationList){
            group.getQualifications().add(qualificationService.findById(q.getQualificationId()));
        }

        var dbGroup = this.repository.save(group);

        List<EventDate>eventDateList=group.getDates();
        group.setDates(new ArrayList<>());
        var dates = new ArrayList<EventDate>();
        for(EventDate e: eventDateList){
            EventDate eventDate = new EventDate();
            eventDate.setDurationInMinutes(e.getDurationInMinutes());
            eventDate.setStartTime(e.getStartTime());
            eventDate.setGroup(dbGroup);
            var dbDate = this.eventDateService.create(eventDate);
            dates.add(dbDate);
        }
        dbGroup.setDates(dates);
        try {
            dbGroup.createEvents(this.calendarService, this.keycloakRestService);
        } catch (IOException e) {
            log.warn("Failed to create calendar events for group {}", group.getAcronym(), e);
        }
        return dbGroup;
    }

    private String generateGroupNumber(String acronym) {
        var groups = this.repository.findByAcronymOrderByGroupNumberDesc(acronym);
        int ret = groups.isEmpty() ? 1 : Integer.parseInt(groups.getFirst().getGroupNumber()) + 1;
        return String.format("%03d", ret);
    }

    @Transactional
    public Group update(Group groupToUpdate, Group group, String jwt) {
        groupToUpdate.setCancelled(group.isCancelled());
        groupToUpdate.setAcronym(group.getAcronym());
        groupToUpdate.setTitle(group.getTitle());
        groupToUpdate.setDescription(group.getDescription());
        groupToUpdate.setNumberOfDates(group.getNumberOfDates());
        groupToUpdate.setNotes(group.getNotes());
        groupToUpdate.setDuration(group.getDuration());
        groupToUpdate.setContact(group.getContact());

        try {
            groupToUpdate.occurrencesModification(
                    CourseMapper.mapEventDatesListToCalendarEventDateSetList(groupToUpdate.getDates()),
                    CourseMapper.mapEventDatesListToCalendarEventDateSetList(group.getDates()),
                    this.calendarService,
                    this.keycloakRestService
            );
        } catch (IOException e) {
            log.warn("Failed to modify calendar events for group {}", group.getAcronym(), e);
        }


        var oldDates = new ArrayList<>(groupToUpdate.getDates());
        var newDates = new ArrayList<>(group.getDates());
        var updatedSavedDate = new ArrayList<EventDate>();
        boolean updatedDates=updateDatesList(oldDates, newDates);

        for (var date: oldDates) {
            if (date.getEventDateId() == 0) {
                date = this.eventDateService.create(date);
                date.setGroup(groupToUpdate);
            }
            updatedSavedDate.add(date);
        }
        groupToUpdate.setDates(updatedSavedDate);
        groupToUpdate.setNumberParticipants(group.getNumberParticipants());
        groupToUpdate.setLocation(group.getLocation());
        groupToUpdate.setMeetingPoint(group.getMeetingPoint());
        groupToUpdate.setTrainerPricePerHour(group.getTrainerPricePerHour());
        groupToUpdate.setPricePerParticipant(group.getPricePerParticipant());
        var qualifications = new ArrayList<>(group.getQualifications());
        groupToUpdate.setQualifications(qualifications);
        groupToUpdate.setParticipantsPerTrainer(group.getParticipantsPerTrainer());
        var trainers = new ArrayList<>(group.getTrainers());
        groupToUpdate.setTrainers(trainers);

        groupToUpdate = this.repository.save(groupToUpdate);
        if(updatedDates){
            mailService.sendGroupChangeMail(groupToUpdate, jwt);
        }
        try {
            groupToUpdate.updateEvents(calendarService, keycloakRestService);
        } catch (IOException e) {
            log.warn("Failed to update calendar events for course {}", groupToUpdate.getAcronym(), e);
        }
        return groupToUpdate;
    }

    private void delete(Group group) {
        this.repository.delete(group);
    }

    public Group deleteById(long id) {
        Optional<Group> data = this.findById(id);
        if (data.isEmpty()) {
            groupNotFoundMessage(id);
            throw new RuntimeException("Group with id" + id + " does not exist");
        }
        Group dbGroup = data.get();
        try {
            dbGroup.deleteEvents(this.calendarService);
        } catch (IOException e) {
            log.warn("Failed to delete calendar events for group {}", dbGroup.getAcronym(), e);
        }
        delete(dbGroup);
        return dbGroup;
    }

    private void groupNotFoundMessage(long id) {
        log.info("Group with id {} does not exist", id);
    }

    private Optional<Group> findById(long id) {
        return this.repository.findById(id);
    }

    public Group get(long id) {
        Optional<Group> data = this.findById(id);
        if (data.isEmpty()) {
            groupNotFoundMessage(id);
            throw new RuntimeException("Group with id " + id + " does not exist");
        }
        return data.get();
    }

    public List<Group> getAll() {
        List<Group> all = this.repository.findAll();
        if (all.isEmpty()) {
            log.info("GroupList is empty");
        }
        return all;
    }

    public Group addTrainer(Group group, Set<Trainer> trainer) {
        var oldTrainers = group.getTrainers();
        oldTrainers.addAll(trainer);
        group.setTrainers(oldTrainers);

        return this.repository.save(group);
    }

    public Group removeTrainer(Group group, Long trainerId) {
        var trainers = group.getTrainers();

        var updatedTrainers = trainers
                .stream()
                .filter(
                        t -> t.getTrainerId() != trainerId
                )
                .toList();

        group.setTrainers(updatedTrainers);
        return this.repository.save(group);
    }

    public Group cancel(Group group, String jwt) {
        group.setCancelled(!group.isCancelled());
        Group canceledGroup = this.repository.save(group);
        mailService.sendGroupCancelMail(canceledGroup, jwt);
        return canceledGroup;
    }

    public boolean updateDatesList(List<EventDate>oldDates, List<EventDate>newDates){
        boolean removedDates=removeUnusedOrUpdatedDates(oldDates, newDates);
        boolean addedNewDates=addNewDates(oldDates,newDates);
        return removedDates||addedNewDates;
    }

    public List<Group>getAllGroupsWithMissingTrainers(){
        return this.repository.findAllWithMissingTrainers();
    }

    private boolean addNewDates(List<EventDate> oldDates, List<EventDate>newDates){
        return oldDates.addAll(newDates);
    }

    public boolean removeUnusedOrUpdatedDates(List<EventDate> oldDates, List<EventDate>newDates){
        boolean removedOldDate = false;
        Iterator<EventDate> iterator=oldDates.iterator();
        while (iterator.hasNext()){
            EventDate oldDate = iterator.next();
            if(checkIfDateIsUnusedOrUpdated(oldDate, newDates)){
                removedOldDate=true;
                iterator.remove();
                this.eventDateService.deleteById(oldDate.getEventDateId());
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
