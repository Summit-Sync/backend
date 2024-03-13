package com.summitsync.api.integrationtest.coursetemplate;

import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetemplate.CourseTemplateRepository;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
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
public class DeleteCourseTemplateIT extends AbstractIntegrationTest {

    @Autowired
    private CourseTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        this.qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        this.repository.save(new CourseTemplate("EK", "Test",2,"test", List.of(Qualification.builder().name("Erste Hilfe Kurs").build())
                ,20, 5, 2,List.of(CourseTemplatePrice.builder().price(BigDecimal.TEN).category("Mitglied").build()), 1000, 90));
    }

    @Test
    public void deleteCourseTemplateHappyPath() throws Exception{
        final var contentAsString=this.mockMvc.perform(delete("/api/v1/coursetemplate/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
