package com.summitsync.api.integrationtest.course;

import com.summitsync.api.MockMVCApiKey;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.location.Location;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.price.Price;
import com.summitsync.api.price.PriceService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.TrainerService;
import com.summitsync.api.trainer.dto.AddTrainerDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseCRUDTest extends AbstractIntegrationTest {

    String participantFirstName = RandomStringUtils.randomAlphabetic(8);
    String participantName = RandomStringUtils.randomAlphabetic(8);
    String waitListParticipantFirstName = RandomStringUtils.randomAlphabetic(8);
    String waitListParticipantName = RandomStringUtils.randomAlphabetic(8);

    String participantFirstName2 = RandomStringUtils.randomAlphabetic(8);
    String participantName2 = RandomStringUtils.randomAlphabetic(8);
    String waitListParticipantFirstName2 = RandomStringUtils.randomAlphabetic(8);
    String waitListParticipantName2 = RandomStringUtils.randomAlphabetic(8);
    @BeforeAll
    void setup(@Autowired QualificationService qualificationService, @Autowired LocationService locationService, @Autowired TrainerService trainerService, @Autowired PriceService priceService) {
        var random = new Random();
        var testQuali1 = Qualification.builder().name("Erste Hilfe").build();
        var testQuali2 = Qualification.builder().name("Zweite Hilfe").build();
        qualificationService.saveQualification(testQuali1);
        qualificationService.saveQualification(testQuali2);
        var location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        locationService.createLocation(location);
        var jwt = MockMVCApiKey.getAccessToken();
        var trainer1 = AddTrainerDto.builder()
                .username("it_test_trainer" + random.nextInt(50000))
                .firstName("Integration")
                .lastName("Test")
                .password("test")
                .email("test@test.test" + random.nextInt(50000))
                .phone("+41241615")
                .build();
        trainerService.newTrainer(trainer1, jwt);
        var trainer2 = AddTrainerDto.builder()
                .username("it_test_trainer" + random.nextInt(50000))
                .firstName("Integration2")
                .lastName("Test")
                .password("test")
                .email("test@test.test" + random.nextInt(50000))
                .phone("+41241615")
                .build();
        trainerService.newTrainer(trainer2, jwt);
        var price1 = Price.builder()
                .price(BigDecimal.valueOf(10.0))
                .name("Test Price 1")
                .build();

        priceService.create(price1);
    }

    @Test
    @Order(1)
    void testCreateCourseHappyPath() throws Exception {
        var participantFirstName = RandomStringUtils.randomAlphabetic(8);
        var participantName = RandomStringUtils.randomAlphabetic(8);

        var waitListParticipantFirstName = RandomStringUtils.randomAlphabetic(8);
        var waitListParticipantName = RandomStringUtils.randomAlphabetic(8);
        var content = String.format("""
{
   "acronym":"kv",
   "dates":[
      "2024-05-13T10:00:25.739Z",
      "2024-05-13T10:00:40.456Z",
      "2024-08-01T09:00:00.000Z"
   ],
   "description":"kvBeschreibrung",
   "duration":40,
   "location":1,
   "meetingPoint":"kvTreffpunkt",
   "notes":"test",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price", "price": 12.3}
   ],
   "requiredQualifications":[
      1,
      2
   ],
   "title":"kvTitle",
   "visible":false,
   "notes": "test",
   "waitList": [
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "participants": [
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "trainers": [2]
}
""",
                waitListParticipantName,
                waitListParticipantFirstName,
                waitListParticipantName,
                waitListParticipantFirstName,
                participantName,
                participantFirstName,
                participantName,
                participantFirstName);

                this.mockMvc.perform(post("/api/v1/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().is(200))
                        .andExpect(jsonPath("id").value(1))
                        .andExpect(jsonPath("visible").value(false))
                        .andExpect(jsonPath("dates.length()").value(3))
                        .andExpect(jsonPath("prices[0].name").value("Test Price"))
                        .andExpect(jsonPath("location.country").value("Germany"))
                        .andExpect(jsonPath("notes").value("test"))
                        .andExpect(jsonPath("trainers.length()").value(1))
                        .andExpect(jsonPath("waitList.length()").value(1))
                        .andExpect(jsonPath("participants.length()").value(1))
                        .andExpect(jsonPath("participants[0].name").value(participantName))
                        .andExpect(jsonPath("waitList[0].firstName").value(waitListParticipantFirstName));

            }
            @Test
            @Order(2)
            void testAddTrainerToCourse() throws Exception {
                var content = """
[1]
""";
        this.mockMvc.perform(put("/api/v1/course/1/trainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("trainers[?(@.firstName == 'Integration')].firstName").value("Integration"))
                .andExpect(jsonPath("trainers.length()").value(2));
    }

    @Test
    @Order(3)
    void testUpdateCourse() throws Exception {
        var participantFirstName = RandomStringUtils.randomAlphabetic(8);
        var participantName = RandomStringUtils.randomAlphabetic(8);

        var waitListParticipantFirstName = RandomStringUtils.randomAlphabetic(8);
        var waitListParticipantName = RandomStringUtils.randomAlphabetic(8);
        var content = String.format("""
{
   "acronym":"kj",
   "dates":[
      "2024-05-13T10:00:25.739Z"
   ],
   "description":"descriptionUpdated",
   "duration":35,
   "location":1,
   "meetingPoint":"kvTreffpunktUpdated",
   "notes":"test updated",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price", "price": 12.3}
   ],
   "requiredQualifications":[
      1,
      2
   ],
   "title":"kvTitle",
   "visible":false,
   "waitList": [
   {"id": 1, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}},
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "participants": [
   {"id": 1, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}},
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "trainers": [1,2]
}
""",
                waitListParticipantName + "UPDATED",
                waitListParticipantFirstName,
                waitListParticipantName,
                waitListParticipantFirstName,
                participantName,
                participantFirstName,
                participantName,
                participantFirstName,
                waitListParticipantName2,
                waitListParticipantFirstName2,
                waitListParticipantName2,
                waitListParticipantFirstName2,
                participantName2,
                participantFirstName2,
                participantName2,
                participantFirstName2);
        this.mockMvc.perform(put("/api/v1/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("acronym").value("kj"))
                .andExpect(jsonPath("dates.length()").value(1))
                .andExpect(jsonPath("meetingPoint").value("kvTreffpunktUpdated"))
                .andExpect(jsonPath("description").value("descriptionUpdated"))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("visible").value(false))
                .andExpect(jsonPath("canceled").value(false))
                .andExpect(jsonPath("dates.length()").value(1))
                .andExpect(jsonPath("prices[0].name").value("Test Price"))
                .andExpect(jsonPath("location.country").value("Germany"))
                .andExpect(jsonPath("notes").value("test updated"))
                .andExpect(jsonPath("duration").value(35))
                .andExpect(jsonPath("trainers[?(@.firstName == 'Integration')].firstName").value("Integration"))
                .andExpect(jsonPath("trainers[?(@.firstName == 'Integration2')].firstName").value("Integration2"))
                .andExpect(jsonPath("waitList.length()").value(2))
                .andExpect(jsonPath("participants.length()").value(2))
                .andExpect(jsonPath("trainers.length()").value(2))
                .andExpect(jsonPath(String.format("waitList[?(@.name == '%s')].name", waitListParticipantName + "UPDATED")).value(waitListParticipantName + "UPDATED"));
    }
    @Test
    @Order(4)
    void testCreateSecondCourseHappyPath() throws Exception {
        var content = """
{
   "acronym":"kv2",
   "dates":[
      "2024-05-13T10:00:25.739Z",
      "2024-05-13T10:00:40.456Z",
      "2024-08-01T09:00:00.000Z"
   ],
   "description":"kvBeschreibrung",
   "duration":40,
   "location":1,
   "meetingPoint":"kvTreffpunkt",
   "notes":"test",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price 2", "price": 12.3}
   ],
   "requiredQualifications":[
      1,
      2
   ],
   "title":"kvTitle",
   "visible":false,
   "notes": "test",
   "waitList": [],
   "participants": [],
   "trainers": []
}
""";
        this.mockMvc.perform(post("/api/v1/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("acronym").value("kv2"))
                .andExpect(jsonPath("visible").value(false))
                .andExpect(jsonPath("dates.length()").value(3))
                .andExpect(jsonPath("prices[0].name").value("Test Price 2"))
                .andExpect(jsonPath("location.country").value("Germany"))
                .andExpect(jsonPath("notes").value("test"));

    }
    @Test
    @Order(5)
    void testGetAllCourses() throws Exception {
        this.mockMvc.perform(get("/api/v1/course")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].trainers.length()").value(2))
                .andExpect(jsonPath("$[0].waitList.length()").value(2))
                .andExpect(jsonPath("$[0].participants.length()").value(2))
                .andExpect(jsonPath("$[0].requiredQualifications.length()").value(2))
                .andExpect(jsonPath("$[0].prices.length()").value(1));
    }
    @Test
    @Order(6)
    void testDeleteCourse() throws Exception {
        this.mockMvc.perform(delete("/api/v1/course/1"))
                .andExpect(status().is(204));

        this.mockMvc.perform(get("/api/v1/course"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
