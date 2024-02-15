package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CourseTemplate")
public class CourseTemplate extends BaseTemplate {
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    @ManyToOne
    private CourseTemplatePrice price;
}
