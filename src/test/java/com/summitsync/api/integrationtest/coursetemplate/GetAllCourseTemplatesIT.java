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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GetAllCourseTemplatesIT extends AbstractIntegrationTest {

    @Autowired
    private CourseTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        repository.save(new CourseTemplate("EK", "Test",2,"test", List.of(Qualification.builder().name("Erste Hilfe Kurs").build())
                ,20, 5, 2,List.of(CourseTemplatePrice.builder().price(BigDecimal.TEN).category("Mitglied").build()), 1000));
        repository.save(new CourseTemplate("FK", "Test1",2,"test", List.of(Qualification.builder().name("Erste Hilfe Kurs").build())
                ,20, 5, 2,List.of(CourseTemplatePrice.builder().price(BigDecimal.TEN).category("Mitglied").build()), 1000));

    }

    @Test
    public void getAllCourseTemplate() throws Exception{
        final var contentAsString=this.mockMvc.perform(get("/api/v1/coursetemplate"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
