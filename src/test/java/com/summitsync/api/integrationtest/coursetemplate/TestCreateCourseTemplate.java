package com.summitsync.api.integrationtest.coursetemplate;

import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.integrationtest.testcontainers.PostgresContextInitializer;
import com.summitsync.api.location.Location;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCreateCourseTemplate extends AbstractIntegrationTest {
    @Autowired
    private QualificationService qualificationService;
    @Autowired
    private LocationService locationService;
    private long quali1Id;
    private long quali2Id;
    private long locationId;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeAll
    void setup() {
        var testQuali1 = Qualification.builder().name("Erste Hilfe").build();
        var testQuali2 = Qualification.builder().name("Zweite Hilfe").build();
        this.quali1Id = this.qualificationService.saveQualification(testQuali1).getQualificationId();
        this.quali2Id = this.qualificationService.saveQualification(testQuali2).getQualificationId();
        var location = Location.builder().country("Germany").phone("+491256321").street("Straße 1").build();
        this.locationId = this.locationService.createLocation(location).getLocationId();
    }

    @AfterAll
    void cleanup() {
        PostgresContextInitializer.cleanAllTables(jdbcTemplate);
    }

    @Test
    @Order(1)
    void testCreateCourseTemplateHappyPath() throws Exception {
        var content = String.format("""
{
    "acronym": "EKA",
    "title": "Course Title",
    "description": "Course description can also be a bit longer!",
    "numberOfDates": 1,
    "duration": 120,
    "numberParticipants": 15,
    "numberWaitlist": 8,
    "meetingPoint": "Big Hall",
    "numberTrainers": 2,
    "requiredQualifications": [%d, %d],
    "location": %d
}
                """, this.quali1Id, this.quali2Id, this.locationId);

        this.mockMvc
                .perform(post("/api/v1/template/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(201))
                .andExpect(jsonPath("acronym").value("EKA"))
                .andExpect(jsonPath("duration").value(120))
                .andExpect(jsonPath("requiredQualifications").isArray())
                .andExpect(jsonPath("requiredQualifications.length()").value(2))
                .andExpect(jsonPath("location.country").value("Germany"));
    }

    @Test
    @Order(2)
    void getAllCourseTemplates() throws Exception {
        this.mockMvc.perform(get("/api/v1/template/course")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].acronym").value("EKA"))
                .andExpect(jsonPath("$[0].numberTrainers").value(2))
                .andExpect(jsonPath("$[0].requiredQualifications.length()").value(2))
                .andExpect(jsonPath("$[0].requiredQualifications[0].name").value("Erste Hilfe"))
                .andExpect(jsonPath("$[0].location.country").value("Germany"))
                .andExpect(jsonPath("$[0].numberOfDates").value(1));
    }

    @Test
    void createCourseInvalidAcronymLength() throws Exception {
        var content = """
{
    "acronym": "EKAAAAAAAAAAAAAAA",
    "title": "Course Title",
    "description": "Course description can also be a bit longer!",
    "numberOfDates": 1,
    "duration": 120,
    "numberParticipants": 15,
    "numberWaitlist": 8,
    "meetingPoint": "Big Hall",
    "numberTrainers": 2,
    "requiredQualifications": [1, 2],
    "location": 1
}
                """;

        this.mockMvc
                .perform(post("/api/v1/template/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("validation"));
    }

    @Test
    @Order(3)
    void updateCourse() throws Exception {
        var content = String.format("""
{
    "acronym": "EKB",
    "title": "Course Title",
    "description": "Course description can also be a bit longer!",
    "numberOfDates": 2,
    "duration": 120,
    "numberParticipants": 15,
    "numberWaitlist": 8,
    "meetingPoint": "Big Hall",
    "numberTrainers": 4,
    "requiredQualifications": [%d],
    "location": %d,
    "price": [{
        "name": "ASD",
        "price": 123.4
    },
    {
        "name": "ASB",
        "price": 125.4
    }]
}
                """, this.quali1Id, this.locationId);
    this.mockMvc.perform(put("/api/v1/template/course/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(jsonPath("acronym").value("EKB"))
            .andExpect(jsonPath("numberTrainers").value(4))
            .andExpect(jsonPath("requiredQualifications.length()").value(1))
            .andExpect(jsonPath("requiredQualifications[0].name").value("Erste Hilfe"))
            .andExpect(jsonPath("numberOfDates").value(2))
            .andExpect(jsonPath("price.length()").value(2))
            .andExpect(jsonPath("price[0].name").value("ASD"));
    }

    @Test
    @Order(4)
    void getAllCourseTemplatesAfterUpdate() throws Exception {
        this.mockMvc.perform(get("/api/v1/template/course")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].acronym").value("EKB"))
                .andExpect(jsonPath("$[0].numberTrainers").value(4))
                .andExpect(jsonPath("$[0].requiredQualifications.length()").value(1))
                .andExpect(jsonPath("$[0].requiredQualifications[0].name").value("Erste Hilfe"))
                .andExpect(jsonPath("$[0].numberOfDates").value(2))
                .andExpect(jsonPath("$[0].price[0].name").value("ASD"))
                .andExpect(jsonPath("$[0].price.length()").value(2));
    }
}
