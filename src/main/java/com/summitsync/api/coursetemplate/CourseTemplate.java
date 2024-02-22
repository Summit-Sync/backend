package com.summitsync.api.coursetemplate;

import com.summitsync.api.basetemplate.BaseTemplate;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "SS_CourseTemplate")
public class CourseTemplate extends BaseTemplate {
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;//>0
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CourseTemplatePrice> priceList;//>0

    public CourseTemplate(long baseTemplateId, String acronym, String title,int numberOfDates, String description, List<Qualification> requiredQualifications, int numberOfParticipants, int numberOfWaitList, int numberOfTrainers, List<CourseTemplatePrice> priceList) {
        super(baseTemplateId, acronym, title,numberOfDates, description, requiredQualifications);
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfWaitList = numberOfWaitList;
        this.numberOfTrainers = numberOfTrainers;
        this.priceList = priceList;
    }

    public CourseTemplate() {
        super();
    }

    public CourseTemplate(String acronym, String title,int numberOfDates, String description, List<Qualification> requiredQualifications, int numberOfParticipants, int numberOfWaitList, int numberOfTrainers, List<CourseTemplatePrice> priceList) {
        super(acronym, title, numberOfDates, description, requiredQualifications);
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfWaitList = numberOfWaitList;
        this.numberOfTrainers = numberOfTrainers;
        this.priceList = priceList;
    }
}
