package com.summitsync.api.integrationtest.utility;

import com.summitsync.api.course.Course;
import com.summitsync.api.course.CourseRepository;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.group.GroupService;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationRepository;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainer.TrainerRepository;
import com.summitsync.api.trainer.TrainerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestScheduledTasks extends AbstractIntegrationTest {

    @Autowired
    private QualificationRepository qualificationRepository;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EventDateService eventDateService;

    private Trainer trainer1;
    private Trainer trainer2;
    private Trainer trainer3;

    @BeforeAll
    void setup(){
        Location location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        Qualification testQuali1 = qualificationRepository.save(Qualification.builder().name("Erste Hilfe").build());
        Qualification testQuali2 = qualificationRepository.save(Qualification.builder().name("Zweite Hilfe").build());
        Qualification testQuali3 = qualificationRepository.save(Qualification.builder().name("Klettern").build());

        trainer1 = trainerRepository.save(Trainer.builder().qualifications(List.of(testQuali2,testQuali1,testQuali3)).subjectId("Tester1").build());
        trainer2 = trainerRepository.save(Trainer.builder().subjectId("Tester2").build());
        trainer3 = trainerRepository.save(Trainer.builder().qualifications(List.of(testQuali1,testQuali2)).subjectId("Tester3").build());
        trainerRepository.saveAll(List.of(trainer1,trainer2,trainer3));
        System.out.println(trainerRepository.findAll().size());

        EventDate date1= eventDateService.create(EventDate.builder().startTime(LocalDateTime.of(2024,7,5,10,0)).build());
        EventDate date2= eventDateService.create(EventDate.builder().startTime(LocalDateTime.of(2024,7,8,10,0)).build());
        EventDate date3= eventDateService.create(EventDate.builder().startTime(LocalDateTime.of(2024,7,14,10,0)).build());
        EventDate date4= eventDateService.create(EventDate.builder().startTime(LocalDateTime.of(2024,7,20,10,0)).build());

        // course for trainer 1 & 3
        Course course1=courseService.create(Course.builder().dates(List.of(date1, date2, date4)).requiredQualifications(List.of(testQuali1)).build());
        // course for trainer 3
        Course course2=courseService.create(Course.builder().dates(List.of(date1, date3, date4)).requiredQualifications(List.of(testQuali3)).build());
        // course not next week
        Course course3=courseService.create(Course.builder().dates(List.of(date1, date4)).requiredQualifications(List.of(testQuali1)).build());

        System.out.println(courseService.getAll().size());
    }

    @Test
    void testCourseMap(){
        LocalDate start= LocalDate.of(2024,7,8);
        LocalDate end=LocalDate.of(2024,7,14);
        Map<Trainer, List<Course>> map=trainerService.findTrainersWithCoursesFulfillingQualificationRequirementMissingTrainersAndBetweenDates(start, end);
        Assertions.assertEquals(map.keySet().size(),2);
        Assertions.assertEquals(map.get(trainer1).size(),1);
        Assertions.assertEquals(map.get(trainer3).size(),2);
        Assertions.assertNull(map.get(trainer2));
    }
}
