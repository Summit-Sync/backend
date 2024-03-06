package com.summitsync.api.integrationtest.grouptemplate;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateRepository;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateRepository;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DeleteGroupTemplateTest extends AbstractIntegrationTest {
    @Autowired
    private GroupTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        var qualifications = List.of(Qualification.builder().name("Erste Hilfe Kurs").build());
        repository.save(new GroupTemplate(new BigDecimal("1.5"), 2, "test title", 5, "test description", qualifications, new BigDecimal("12.5")));
    }

    @Test
    public void deleteGroupTemplateHappyPath() throws Exception{
        final var contentAsString=this.mockMvc.perform(delete("/api/v1/grouptemplate/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
