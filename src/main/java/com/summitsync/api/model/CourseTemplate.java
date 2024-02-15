package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseTemplate extends BaseTemplate {
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    @ManyToOne
    private CourseTemplatePrice price;
}
