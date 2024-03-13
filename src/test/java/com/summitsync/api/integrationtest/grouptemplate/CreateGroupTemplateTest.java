package com.summitsync.api.integrationtest.grouptemplate;

import com.summitsync.api.grouptemplate.GroupTemplateRepository;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateGroupTemplateTest extends AbstractIntegrationTest {

    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        this.qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
    }
    @Test
    public void createGroupTemplateHappyPath() throws Exception{
        String content= """
                {
                    "acronym":"GK",
                    "title":"test",
                    "description":"test",
                    "qualificationList":[
                        {
                            "qualificationId":1,
                            "name":"Erste Hilfe Kurs"
                        }
                    ],
                    "trainerKey":1,
                    "pricePerTrainerPerHour":1.5
                }
                """;
        final var contentAsString=this.mockMvc.perform(post("/api/v1/grouptemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id",greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("acronym").value("GK"))
                .andExpect(jsonPath("title").value("test"));
    }
}
