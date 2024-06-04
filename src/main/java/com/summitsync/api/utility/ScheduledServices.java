package com.summitsync.api.utility;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.GroupService;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduledServices {

    private final CourseService courseService;
    private final GroupService groupService;
    private final MailService mailService;
    private final TrainerService trainerService;
    private final KeycloakRestService keyCloakRestService;

    @Scheduled(cron = "0 0 * * * *")
    public void sendReminderMailForCourses(){
        List<Course>coursesForNextDay=courseService.getAll();
        coursesForNextDay=coursesForNextDay.stream().filter(this::courseTakesPlaceTheNextDay).toList();
        coursesForNextDay.forEach(course -> mailService.sendCourseReminderMail(course, keyCloakRestService.getJwt()));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sendReminderMailForGroups(){
        List<Group>groupsForNextDay=groupService.getAll();
        groupsForNextDay=groupsForNextDay.stream().filter(this::groupTakesPlaceTheNextDay).toList();
        groupsForNextDay.forEach(group -> mailService.sendGroupReminderMail(group, keyCloakRestService.getJwt()));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cancelCoursesAutomatically(){
        List<Course>courses=courseService.getAll();
        courses=courses.stream().filter(course -> course.getParticipants().size()<=1).toList();
        courses.forEach(course -> mailService.sendCourseCancelMail(course, keyCloakRestService.getJwt()));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cancelGroupsAutomatically(){
        List<Group>groups=groupService.getAll();
        groups.stream()
                .filter(group -> group.getTrainers().size()<group.getNumberParticipants()/group.getParticipantsPerTrainer())
                .forEach(group -> mailService.sendGroupCancelMail(group, keyCloakRestService.getJwt()));
    }

    @Scheduled(cron = "0 0 * * * MON")
    public void sendMailForMissingTrainers(){
        LocalDate startDate=LocalDate.now().plusDays(7);
        LocalDate endDate=LocalDate.now().plusDays(14);
        Map<Trainer,List<Course>>courseMap=trainerService.findTrainersWithCoursesFulfillingQualificationRequirementMissingTrainersAndBetweenDates(startDate, endDate);
        Map<Trainer, List<Group>>groupMap=trainerService.findTrainersWithGroupsFulfillingQualificationRequirementMissingTrainersAndBetweenDates(startDate, endDate);
        List<Trainer>trainerList=trainerService.getAllTrainer();
        mailService.sendOpenTrainerSpotTrainerMail(trainerList, courseMap, groupMap, keyCloakRestService.getJwt());
    }

    private boolean isQualificationRequirementFulfilled(List<Qualification> requiredQualifications, Trainer trainer){
        for(Qualification qualification: requiredQualifications){
            if(!trainerHasQualification(qualification,trainer)){
                return false;
            }
        }
        return true;
    }

    private boolean trainerHasQualification(Qualification qualification, Trainer trainer){
        for(Qualification trainerQualification: trainer.getQualifications()){
            if(trainerQualification.getName().equals(qualification.getName())){
                return true;
            }
        }
        return false;
    }

    private boolean courseTakesPlaceTheNextDay(Course course){
        for(EventDate date: course.getDates()){
            if(date.getStartTime().toLocalDate().isEqual(LocalDate.now().plusDays(1))){
                return true;
            }
        }
        return false;
    }

    private boolean groupTakesPlaceTheNextDay(Group group){
        for(EventDate date: group.getDates()){
            if(date.getStartTime().toLocalDate().isEqual(LocalDate.now().plusDays(1))){
                return true;
            }
        }
        return false;
    }
}
