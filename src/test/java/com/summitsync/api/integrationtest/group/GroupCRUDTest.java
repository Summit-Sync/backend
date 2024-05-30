package com.summitsync.api.integrationtest.group;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupCRUDTest extends AbstractIntegrationTest {
    @BeforeAll
    static void setup(
            @Autowired QualificationService qualificationService,
            @Autowired LocationService locationService,
            @Autowired TrainerService trainerService,
            @Autowired PriceService priceService
    ) {
        var random = new Random();
        var testQuali1 = Qualification.builder().name("Erste Hilfe").build();
        var testQuali2 = Qualification.builder().name("Zweite Hilfe").build();
        qualificationService.saveQualification(testQuali1);
        qualificationService.saveQualification(testQuali2);
        var location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        locationService.createLocation(location);
        var jwt = MockMVCApiKey.getAccessToken();
        var trainer1 = AddTrainerDto.builder()
                .username("it_test_trainer_trainer1_group" + random.nextInt(5000))
                .firstName("Integration")
                .lastName("Test")
                .password("test")
                .email("test@test.test.trainer1.group" + random.nextInt(5000))
                .phone("+41241615")
                .build();
        trainerService.newTrainer(trainer1, jwt);
        var trainer2 = AddTrainerDto.builder()
                .username("it_test_trainer_trainer2_group" + random.nextInt(5000))
                .firstName("Integration2")
                .lastName("Test2")
                .password("test")
                .email("test@test.test.trainer2.group" + random.nextInt(5000))
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
    void testCreateGroupHappyPath() throws Exception {
        var content = """
{
   "title":"Test Gruppe1",
   "description":"kvBeschreibrung",
   "numberOfDates": 3,
   "events":[
      "2024-05-13T10:00:25.739Z",
      "2024-05-13T10:00:40.456Z",
      "2024-08-01T09:00:00.000Z"
   ],
   "duration":40,
   "numberParticipants":2,
   "contact": {
        "firstName": "asd",
        "lastName": "asd2",
        "email": "e@mail.com",
        "phone": "1234"
   },
   "location":1,
   "meetingPoint":"kvTreffpunkt",
   "trainerPricePerHour": "123.4",
   "pricePerParticipant": "123.4",
   "requiredQualifications":[
      1,
      2
   ],
   "participantsPerTrainer": 10,
   "trainers": [1],
   "acronym": "ts"
}
""";
        this.mockMvc.perform(post("/api/v1/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("dates.length()").value(3))
                .andExpect(jsonPath("requiredQualifications.length()").value(2))
                .andExpect(jsonPath("location.country").value("Germany"));

        this.mockMvc.perform(get("/api/v1/group/1")).andExpect(jsonPath("trainers.length()").value(1));

    }
    @Test
    @Order(2)
    @Disabled
    void testAddTrainerToGroup() throws Exception {
        var content = """
[2]
""";
        this.mockMvc.perform(put("/api/v1/group/1/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("trainers[0].firstName").value("Integration"))
                .andExpect(jsonPath("trainers.length()").value(1));

        this.mockMvc.perform(get("/api/v1/group/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("trainers.length()").value(2));
    }

    @Test
    @Order(3)
    void testUpdateGroup() throws Exception {
        var content = """
{
   "title":"Test Gruppe1",
   "description":"kvBeschreibrungUpdated",
   "numberOfDates": 2,
   "events":[
      "2024-05-13T10:00:25.739Z",
      "2024-05-13T10:00:40.456Z"
   ],
   "duration":45,
   "numberParticipants":2,
   "contact": {
        "firstName": "asd",
        "lastName": "asd2",
        "email": "e@mail.com",
        "phone": "1234"
   },
   "location":1,
   "meetingPoint":"kvTreffpunkt",
   "trainerPricePerHour": "123.4",
   "pricePerParticipant": "123.4",
   "requiredQualifications":[
      1,
      2
   ],
   "participantsPerTrainer": 10,
   "trainers": [1,2],
   "acronym": "ts"
}
""";
        this.mockMvc.perform(put("/api/v1/group/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("dates.length()").value(2))
                .andExpect(jsonPath("location.country").value("Germany"))
                .andExpect(jsonPath("duration").value(45))
                .andExpect(jsonPath("groupNumber").value("001"))
                .andExpect(jsonPath("trainers[?(@.firstName == 'Integration')].firstName").value("Integration"))
                .andExpect(jsonPath("trainers.length()").value(2));
    }

    @Test
    @Order(4)
    void testCreateSecondGroupHappyPath() throws Exception {
        var content = """
{
   "title":"Test Gruppe2",
   "description":"kvBeschreibrung",
   "numberOfDates": 1,
   "events":[
      "2024-08-01T09:00:00.000Z"
   ],
   "duration":5,
   "numberParticipants":2,
   "contact": {
        "firstName": "asd",
        "lastName": "asd2",
        "email": "e@mail.com",
        "phone": "1234"
   },
   "location":1,
   "meetingPoint":"kvTreffpunkt",
   "trainerPricePerHour": "123.4",
   "pricePerParticipant": "123.4",
   "requiredQualifications":[
      1,
      2
   ],
   "participantsPerTrainer": 10,
   "trainers": [1],
   "acronym": "ts"
}
""";
        this.mockMvc.perform(post("/api/v1/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("acronym").value("ts"))
                .andExpect(jsonPath("groupNumber").value("002"))
                .andExpect(jsonPath("dates.length()").value(1))
                .andExpect(jsonPath("location.country").value("Germany"));

    }

    @Test
    @Order(5)
    void testGetAllGroups() throws Exception {
        this.mockMvc.perform(get("/api/v1/group")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].requiredQualifications.length()").value(2))
                .andExpect(jsonPath("$[0].contact.firstName").value("asd"))
                .andExpect(jsonPath("$[0].trainers.length()").value(2))
                .andExpect(jsonPath("$[0].dates.length()").value(2));
    }

    @Test
    @Order(6)
    void testDeleteGroup() throws Exception {
        this.mockMvc.perform(delete("/api/v1/group/1"))
                .andExpect(status().is(204));

        this.mockMvc.perform(get("/api/v1/group"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
