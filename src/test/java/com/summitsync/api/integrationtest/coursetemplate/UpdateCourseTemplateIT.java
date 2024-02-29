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
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UpdateCourseTemplateIT extends AbstractIntegrationTest {

    @Autowired
    private CourseTemplateRepository repository;
    @Autowired
    private QualificationRepository qualificationRepository;

    @BeforeEach
    public void setUp() throws Exception{
        qualificationRepository.save(Qualification.builder().name("Erste Hilfe Kurs").build());
        repository.save(new CourseTemplate("EK", "Test",2,"test",List.of(Qualification.builder().name("Erste Hilfe Kurs").build())
                ,20, 5, 2,List.of(CourseTemplatePrice.builder().price(BigDecimal.TEN).category("Mitglied").build()), 1000));
    }

    @Test
    public void updateCourseTemplateHappyPath() throws Exception{
        String content = """
                {
                    "id":1,
                    "acronym":"EK",
                    "title":"test",
                    "duration":10000,
                    "numberOfDates":"2",
                    "description":"test",
                    "qualificationList":[
                        {
                            "qualificationId":1,
                            "name":"Erste Hilfe Kurs"
                        }
                    ],
                    "numberOfParticipants":10,
                    "numberOfWaitList":5,
                    "numberOfTrainers":1,
                    "priceList":[
                        {
                            "price":10,
                            "category":"Mitglied"
                        }
                    ]
                }
                """;

        final var contentAsString=this.mockMvc.perform(put("/api/v1/coursetemplate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id",is(1)));
    }

}
