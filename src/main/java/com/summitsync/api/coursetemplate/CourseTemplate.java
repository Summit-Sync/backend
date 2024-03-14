package com.summitsync.api.coursetemplate;

import com.summitsync.api.basetemplate.BaseTemplate;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "SS_CourseTemplate")
public class CourseTemplate extends BaseTemplate {
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;//>0
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<CourseTemplatePrice> priceList;//>0

    public CourseTemplate() {
        super();
    }

    public CourseTemplate(String acronym, String title,int numberOfDates, String description, Set<Qualification> requiredQualifications, int numberOfParticipants, int numberOfWaitList, int numberOfTrainers, Set<CourseTemplatePrice> priceList, int duration, int numberOfMinutesPerDate) {
        super(acronym, title, numberOfDates, description, requiredQualifications, duration, numberOfMinutesPerDate);
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfWaitList = numberOfWaitList;
        this.numberOfTrainers = numberOfTrainers;
        this.priceList = priceList;
    }
}
