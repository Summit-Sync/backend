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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GetAllGroupTemplateTest extends AbstractIntegrationTest {
    @Autowired
    private GroupTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        this.qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        this.repository.save(new GroupTemplate(new BigDecimal("1.5"), 2, "test title", 5, "test description", List.of(Qualification.builder().name("Erste Hilfe Kurs").build()), new BigDecimal("12.5")));
        this.repository.save(new GroupTemplate(new BigDecimal("1.5"), 2, "test title 2", 5, "test description 2", List.of(Qualification.builder().name("Erste Hilfe Kurs").build()), new BigDecimal("12.5")));
    }

    @Test
    public void getAllGroupTemplates() throws Exception {
        final var contentAsString = this.mockMvc.perform(get("/api/v1/grouptemplate"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
