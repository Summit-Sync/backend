package com.summitsync.api.integrationtest.utility;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.group.Group;
import com.summitsync.api.group.GroupService;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.integrationtest.testcontainers.PostgresContextInitializer;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationRepository;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerRepository;
import com.summitsync.api.utility.ScheduledServices;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestScheduledTasks extends AbstractIntegrationTest {

    @Autowired
    private QualificationRepository qualificationRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EventDateService eventDateService;
    @Autowired
    private ScheduledServices scheduledServices;

    private Trainer trainer1;
    private Trainer trainer2;
    private Trainer trainer3;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeAll
    void setup(){
        Qualification testQuali1 = qualificationRepository.save(Qualification.builder().name("Erste Hilfe").build());
        Qualification testQuali2 = qualificationRepository.save(Qualification.builder().name("Zweite Hilfe").build());
        Qualification testQuali3 = qualificationRepository.save(Qualification.builder().name("Klettern").build());

        trainer1 = trainerRepository.save(Trainer.builder().qualifications(List.of(testQuali2,testQuali1,testQuali3)).subjectId("Tester1").build());
        trainer2 = trainerRepository.save(Trainer.builder().subjectId("Tester2").build());
        trainer3 = trainerRepository.save(Trainer.builder().qualifications(List.of(testQuali1,testQuali2)).subjectId("Tester3").build());
        trainerRepository.saveAll(List.of(trainer1,trainer2,trainer3));
        System.out.println(trainerRepository.findAll().size());
        //valid dates
        EventDate date1a= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(7)).build());
        EventDate date1b= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(7)).build());
        EventDate date1d= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(7)).build());
        EventDate date1e= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(7)).build());
        //valid dates
        EventDate date2= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(14)).build());
        EventDate date2a= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(14)).build());
        EventDate date2b= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(14)).build());
        EventDate date2c= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(14)).build());
        //valid dates
        EventDate date3= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(10)).build());
        EventDate date3a= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(10)).build());
        //invalid dates
        EventDate date4a= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4b= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4c= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4d= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4e= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4f= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4g= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());
        EventDate date4h= eventDateService.create(EventDate.builder().startTime(LocalDateTime.now().plusDays(15)).build());

        // course for trainer 1 & 3
        courseService.create(Course.builder().dates(List.of(date1a, date2, date4a)).requiredQualifications(List.of(testQuali1)).trainers(new ArrayList<>()).numberTrainer(2).build());
        // course for trainer 1
        courseService.create(Course.builder().dates(List.of(date1b, date3, date4b)).requiredQualifications(List.of(testQuali3)).trainers(new ArrayList<>()).numberTrainer(2).build());
        // course not next week
        courseService.create(Course.builder().dates(List.of(date4c)).requiredQualifications(List.of(testQuali1)).trainers(new ArrayList<>()).numberTrainer(2).build());
        // course has enough trainers
        courseService.create(Course.builder().dates(List.of(date2a, date4d)).requiredQualifications(List.of(testQuali1)).trainers(new ArrayList<>()).numberTrainer(0).build());

        // group for trainer 1 & 3
        groupService.create(Group.builder().dates(List.of(date1d, date2b, date4e)).qualifications(List.of(testQuali1)).trainers(new ArrayList<>()).participantsPerTrainer(1).numberParticipants(2).build());
        // group for trainer 1
        groupService.create(Group.builder().dates(List.of(date1e, date3a, date4f)).qualifications(List.of(testQuali3)).trainers(new ArrayList<>()).participantsPerTrainer(1).numberParticipants(2).build());
        // group not next week
        groupService.create(Group.builder().dates(List.of(date4g)).qualifications(List.of(testQuali1)).trainers(new ArrayList<>()).participantsPerTrainer(1).numberParticipants(2).build());
        // group has enough trainers
        groupService.create(Group.builder().dates(List.of(date2c, date4h)).qualifications(List.of(testQuali1)).trainers(new ArrayList<>()).participantsPerTrainer(1).numberParticipants(0).build());
    }

    @AfterAll
    void cleanup() {
        PostgresContextInitializer.cleanAllTables(jdbcTemplate);
    }

    @Test
    void testCourseMap(){
        Map<Trainer, List<Course>> map=scheduledServices.createCourseMap();
        Assertions.assertEquals(map.keySet().size(),2);
        Assertions.assertEquals(map.get(trainer1).size(),2);
        Assertions.assertEquals(map.get(trainer3).size(),1);
        Assertions.assertNull(map.get(trainer2));
    }

    @Test
    void testGroupMap(){
        Map<Trainer, List<Group>> map = scheduledServices.createGroupMap();
        Assertions.assertEquals(map.keySet().size(),2);
        Assertions.assertEquals(map.values().size(),2);
        Assertions.assertEquals(map.get(trainer3).size(),1);
        Assertions.assertNull(map.get(trainer2));
    }
}
