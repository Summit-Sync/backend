package com.summitsync.api.coursetemplate;

import com.summitsync.api.basetemplate.BaseTemplate;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTemplatePrice price;
}
