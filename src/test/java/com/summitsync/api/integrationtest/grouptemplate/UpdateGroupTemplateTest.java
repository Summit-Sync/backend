package com.summitsync.api.integrationtest.grouptemplate;

import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateRepository;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UpdateGroupTemplateTest extends AbstractIntegrationTest {

    @Autowired
    private GroupTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception {
        this.qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        this.repository.save(new GroupTemplate(new BigDecimal("1.5"), 2, "test title", 5, "test description", List.of(Qualification.builder().name("Erste Hilfe Kurs").build()), new BigDecimal("12.5")));
    }

    @Test
    public void updateGroupTemplate() throws Exception {
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

        final var contentAsString=this.mockMvc.perform(put("/api/v1/grouptemplate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id",is(1)));
    }
}
