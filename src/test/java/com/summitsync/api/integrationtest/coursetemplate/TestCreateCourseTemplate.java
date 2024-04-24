package com.summitsync.api.integrationtest.coursetemplate;

import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.location.Location;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TestCreateCourseTemplate extends AbstractIntegrationTest {
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
    }

    @Test
    void testCreateCourseHappyPath() throws Exception {
        var content = """
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
    "requiredQualifications": [1, 2],
    "location": 1
}
                """;

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
}
