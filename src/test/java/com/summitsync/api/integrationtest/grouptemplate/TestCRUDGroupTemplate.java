package com.summitsync.api.integrationtest.grouptemplate;

import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.location.Location;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCRUDGroupTemplate extends AbstractIntegrationTest {
    @Autowired
    private QualificationService qualificationService;
    @Autowired
    private LocationService locationService;
    @BeforeEach
    void setup() {
        var testQuali1 = Qualification.builder().name("Erste Hilfe").build();
        var testQuali2 = Qualification.builder().name("Zweite Hilfe").build();
        this.qualificationService.saveQualification(testQuali1);
        this.qualificationService.saveQualification(testQuali2);
        var location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        this.locationService.createLocation(location);
        var location2 = Location.builder().country("Germany2").phone("+491256321").street("Straße 1").build();
        this.locationService.createLocation(location2);
    }

    @Test
    @Order(1)
    void testCreateCourseTemplateHappyPath() throws Exception {
        var content = """
{
    "acronym": "ACR",
    "title": "Group Template Title",
    "description": "Group Template description",
    "numberOfDates": 4,
    "duration": 120,
    "location": 1,
    "meetingPoint": "Meeting Point for Group",
    "trainerPricePerHour": 35.3,
    "pricePerParticipant": 25.3,
    "requiredQualificationList": [1,2],
    "participantsPerTrainer": 2
}
                """;

        this.mockMvc
                .perform(post("/api/v1/template/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200))
                .andExpectAll(
                        jsonPath("acronym").value("ACR"),
                        jsonPath("title").value("Group Template Title"),
                        jsonPath("numberOfDates").value(4),
                        jsonPath("duration").value(120),
                        jsonPath("location.country").value("Germany"),
                        jsonPath("meetingPoint").value("Meeting Point for Group"),
                        jsonPath("trainerPricePerHour").value(35.3),
                        jsonPath("pricePerParticipant").value(25.3),
                        jsonPath("requiredQualificationList.length()").value(2),
                        jsonPath("requiredQualificationList[?(@.name == 'Erste Hilfe')].name").value("Erste Hilfe"),
                        jsonPath("participantsPerTrainer").value(2)
                        );
    }

    @Test
    @Order(2)
    void getAllCourseTemplates() throws Exception {
        this.mockMvc.perform(get("/api/v1/template/group")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].acronym").value("ACR"),
                        jsonPath("$[0].title").value("Group Template Title"),
                        jsonPath("$[0].numberOfDates").value(4),
                        jsonPath("$[0].duration").value(120),
                        jsonPath("$[0].location.country").value("Germany"),
                        jsonPath("$[0].meetingPoint").value("Meeting Point for Group"),
                        jsonPath("$[0].trainerPricePerHour").value(35.3),
                        jsonPath("$[0].pricePerParticipant").value(25.3),
                        jsonPath("$[0].requiredQualificationList.length()").value(2),
                        jsonPath("$[0].requiredQualificationList[?(@.name == 'Erste Hilfe')].name").value("Erste Hilfe"),
                        jsonPath("$[0].participantsPerTrainer").value(2)
                );
    }

    @Test
    void createGroupTemplateInvalidAcronymLength() throws Exception {
        var content = """
{
    "acronym": "ACRAAAAAAAAA",
    "title": "Group Template Title",
    "description": "Group Template description",
    "numberOfDates": 4,
    "duration": 120,
    "location": 1,
    "meetingPoint": "Meeting Point for Group",
    "trainerPricePerHour": 35.3,
    "pricePerParticipant": 25.3,
    "requiredQualificationList": [1,2],
    "participantsPerTrainer": 2
}
                """;


        this.mockMvc
                .perform(post("/api/v1/template/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("validation"));
    }

    @Test
    @Order(3)
    void updateCourse() throws Exception {
        var content = """
{
    "acronym": "BCR",
    "title": "Group Template Title UPDATED",
    "description": "Group Template description",
    "numberOfDates": 2,
    "duration": 130,
    "location": 1,
    "meetingPoint": "Meeting Point for Group UPDATED",
    "trainerPricePerHour": 45.3,
    "pricePerParticipant": 45.3,
    "requiredQualificationList": [1],
    "participantsPerTrainer": 1
}
                """;
        this.mockMvc.perform(put("/api/v1/template/group/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpectAll(
                jsonPath("acronym").value("BCR"),
                jsonPath("title").value("Group Template Title UPDATED"),
                jsonPath("numberOfDates").value(2),
                jsonPath("duration").value(130),
                jsonPath("location.country").value("Germany"),
                jsonPath("meetingPoint").value("Meeting Point for Group UPDATED"),
                jsonPath("trainerPricePerHour").value(45.3),
                jsonPath("pricePerParticipant").value(45.3),
                jsonPath("requiredQualificationList.length()").value(1),
                jsonPath("requiredQualificationList[?(@.name == 'Erste Hilfe')].name").value("Erste Hilfe"),
                jsonPath("participantsPerTrainer").value(1)
        );
    }

    @Test
    @Order(4)
    void getAllGroupTemplatesAfterUpdate() throws Exception {
        this.mockMvc.perform(get("/api/v1/template/group/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("acronym").value("BCR"),
                        jsonPath("title").value("Group Template Title UPDATED"),
                        jsonPath("numberOfDates").value(2),
                        jsonPath("duration").value(130),
                        jsonPath("location.country").value("Germany"),
                        jsonPath("meetingPoint").value("Meeting Point for Group UPDATED"),
                        jsonPath("trainerPricePerHour").value(45.3),
                        jsonPath("pricePerParticipant").value(45.3),
                        jsonPath("requiredQualificationList.length()").value(1),
                        jsonPath("requiredQualificationList[?(@.name == 'Erste Hilfe')].name").value("Erste Hilfe"),
                        jsonPath("participantsPerTrainer").value(1)
                );
    }
}
