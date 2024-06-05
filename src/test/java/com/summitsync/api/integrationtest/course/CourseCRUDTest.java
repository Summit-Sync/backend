package com.summitsync.api.integrationtest.course;

import com.jayway.jsonpath.JsonPath;
import com.summitsync.api.MockMVCApiKey;
import com.summitsync.api.TestSummitSyncApplication;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseCRUDTest extends AbstractIntegrationTest {

    String participantFirstName = RandomStringUtils.randomAlphabetic(15);
    String participantName = RandomStringUtils.randomAlphabetic(15);
    String waitListParticipantFirstName = RandomStringUtils.randomAlphabetic(15);
    String waitListParticipantName = RandomStringUtils.randomAlphabetic(15);

    String participantFirstName2 = RandomStringUtils.randomAlphabetic(15);
    String participantName2 = RandomStringUtils.randomAlphabetic(15);
    String waitListParticipantFirstName2 = RandomStringUtils.randomAlphabetic(15);
    String waitListParticipantName2 = RandomStringUtils.randomAlphabetic(15);

    int waitList1Id;
    int participant1Id;

    private long qualiId1;
    private long qualiId2;
    private long locationId1;
    private long trainerId1;
    private long trainerId2;
    private long price1Id;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeAll
    void setup(@Autowired QualificationService qualificationService, @Autowired LocationService locationService, @Autowired TrainerService trainerService, @Autowired PriceService priceService) {
        var random = new Random();
        var testQuali1 = Qualification.builder().name("Erste Hilfe").build();
        var testQuali2 = Qualification.builder().name("Zweite Hilfe").build();
        this.qualiId1 = qualificationService.saveQualification(testQuali1).getQualificationId();
        this.qualiId2 = qualificationService.saveQualification(testQuali2).getQualificationId();
        var location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        this.locationId1 = locationService.createLocation(location).getLocationId();
        var jwt = MockMVCApiKey.getAccessToken();
        var trainer1 = AddTrainerDto.builder()
                .username("it_test_trainer" + random.nextInt(50000))
                .firstName("Integration")
                .lastName("Test")
                .password("test")
                .email("test@test.test" + random.nextInt(50000))
                .phone("+41241615")
                .build();
        this.trainerId1 = trainerService.newTrainer(trainer1, jwt).getId();
        var trainer2 = AddTrainerDto.builder()
                .username("it_test_trainer" + random.nextInt(50000))
                .firstName("Integration2")
                .lastName("Test")
                .password("test")
                .email("test@test.test" + random.nextInt(50000))
                .phone("+41241615")
                .build();
        this.trainerId2 = trainerService.newTrainer(trainer2, jwt).getId();
        var price1 = Price.builder()
                .price(BigDecimal.valueOf(10.0))
                .name("Test Price 1")
                .build();

        this.price1Id = priceService.create(price1).getCourseTemplatePriceId();
    }

    @AfterAll
    void cleanup() {
        TestSummitSyncApplication.cleanAllTables(jdbcTemplate);
    }

    @Test
    @Order(1)
    void testCreateCourseHappyPath() throws Exception {
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
   "location":%d,
   "meetingPoint":"kvTreffpunkt",
   "notes":"test",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price", "price": 12.3}
   ],
   "requiredQualifications":[
      %d,
      %d
   ],
   "title":"kvTitle",
   "visible":false,
   "notes": "test",
   "waitList": [
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "participants": [
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}, "phone": "123"}
   ],
   "trainers": [%d]
}
""",
                this.locationId1,
                this.qualiId1,
                this.qualiId2,
                waitListParticipantName,
                waitListParticipantFirstName,
                waitListParticipantName,
                waitListParticipantFirstName,
                participantName,
                participantFirstName,
                participantName,
                participantFirstName,
                this.trainerId2);

                var result = this.mockMvc.perform(post("/api/v1/course")
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
                        .andExpect(jsonPath("participants[0].phone").value("123"))
                        .andExpect(jsonPath("waitList[0].firstName").value(waitListParticipantFirstName))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

                this.waitList1Id = JsonPath.read(result, "$.waitList[0].id");
                this.participant1Id = JsonPath.read(result, "$.participants[0].id");
            }
            @Test
            @Order(2)
            void testAddTrainerToCourse() throws Exception {
                var content = String.format("""
[%d]
""", trainerId1);
        this.mockMvc.perform(put("/api/v1/course/1/trainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("trainers[?(@.firstName == 'Integration')].firstName").value("Integration"))
                .andExpect(jsonPath("participants[0].phone").value(123))
                .andExpect(jsonPath("trainers.length()").value(2));
    }

    @Test
    @Order(3)
    void testUpdateCourse() throws Exception {
        var content = String.format("""
{
   "acronym":"kj",
   "dates":[
      "2024-05-13T10:00:25.739Z"
   ],
   "description":"descriptionUpdated",
   "duration":35,
   "location":%d,
   "meetingPoint":"kvTreffpunktUpdated",
   "notes":"test updated",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price", "price": 12.3}
   ],
   "requiredQualifications":[
      %d,
      %d
   ],
   "title":"kvTitle",
   "visible":false,
   "waitList": [
   {"id": %d, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}},
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}}
   ],
   "participants": [
   {"id": %d, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}, "phone": "12345"},
   {"id": 0, "name": "%s", "firstName": "%s", "email": "%s@%s.com", "status": {"text": "text"}, "phone": "123"}
   ],
   "trainers": [%d,%d]
}
""",
                this.locationId1,
                this.qualiId1,
                this.qualiId2,
                this.waitList1Id,
                waitListParticipantName + "UPDATED",
                waitListParticipantFirstName,
                waitListParticipantName + "UPDATED",
                waitListParticipantFirstName,
                waitListParticipantName2,
                waitListParticipantFirstName2,
                waitListParticipantName2,
                waitListParticipantFirstName2,
                this.participant1Id,
                participantName,
                participantFirstName,
                participantName,
                participantFirstName,
                participantName2,
                participantFirstName2,
                participantName2,
                participantFirstName2,
                this.trainerId1,
                this.trainerId2);
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
                .andExpect(jsonPath(String.format("participants[?(@.firstName == '%s')].phone", participantFirstName)).value("12345"))
                .andExpect(jsonPath("waitList.length()").value(2))
                .andExpect(jsonPath("participants.length()").value(2))
                .andExpect(jsonPath("trainers.length()").value(2))
                .andExpect(jsonPath(String.format("waitList[?(@.name == '%s')].name", waitListParticipantName + "UPDATED")).value(waitListParticipantName + "UPDATED"));
    }
    @Test
    @Order(4)
    void testCreateSecondCourseHappyPath() throws Exception {
        var content = String.format("""
{
   "acronym":"kv2",
   "dates":[
      "2024-05-13T10:00:25.739Z",
      "2024-05-13T10:00:40.456Z",
      "2024-08-01T09:00:00.000Z"
   ],
   "description":"kvBeschreibrung",
   "duration":40,
   "location":%d,
   "meetingPoint":"kvTreffpunkt",
   "notes":"test",
   "numberParticipants":2,
   "numberTrainers":2,
   "numberWaitlist":2,
   "prices":[
      {"name": "Test Price 2", "price": 12.3}
   ],
   "requiredQualifications":[
      %d,
      %d
   ],
   "title":"kvTitle",
   "visible":false,
   "notes": "test",
   "waitList": [],
   "participants": [],
   "trainers": []
}
""",
                this.locationId1,
                this.qualiId1,
                this.qualiId2);
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
                .andExpect(jsonPath(String.format("$[0].participants[?(@.firstName == '%s')].phone", participantFirstName)).value("12345"))
                .andExpect(jsonPath("$[0].requiredQualifications.length()").value(2))
                .andExpect(jsonPath("$[0].dates.length()").value(1))
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
