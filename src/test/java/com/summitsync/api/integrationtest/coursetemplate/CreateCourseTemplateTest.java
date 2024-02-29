package com.summitsync.api.integrationtest.coursetemplate;

import com.summitsync.api.coursetemplate.CourseTemplateRepository;
import com.summitsync.api.integrationtest.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateCourseTemplateTest extends AbstractIntegrationTest {

    @Autowired
    private CourseTemplateRepository repository;

    @Test
    public void createCourseTemplateHappyPath() throws Exception{
        String content= """
                {
                    "acronym":"EK",
                    "title":"test",
                    "duration":10000,
                    "numberOfDates":"2",
                    "description":"test",
                    "qualificationList":[
                        {
                            "id":1,
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
                    ],
                    "duration":100000
                }
                """;
        final var contentAsString=this.mockMvc.perform(post("/api/v1/coursetemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id",is(1)));
    }

    @Test
    public void createCourseTemplateMissingTitle() throws Exception{
        String content= """
                {
                    "acronym":"EK",
                    "duration":10000,
                    "numberOfDates":"2",
                    "description":"test",
                    "qualificationList":[
                        {
                            "id":1,
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
                    ],
                    "duration":10000
                }
                """;
        final var contentAsString=this.mockMvc.perform(post("/api/v1/coursetemplate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }
}
