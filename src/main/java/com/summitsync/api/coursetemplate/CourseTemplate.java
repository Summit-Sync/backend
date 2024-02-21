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
    private int numberOfTrainers;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CourseTemplatePrice> priceList;

    public CourseTemplate(long baseTemplateId, String acronym, int numberOfDates, String description, List<Qualification> requiredQualifications, int numberOfParticipants, int numberOfWaitList, int numberOfTrainers, List<CourseTemplatePrice> priceList) {
        super(baseTemplateId, acronym, numberOfDates, description, requiredQualifications);
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfWaitList = numberOfWaitList;
        this.numberOfTrainers = numberOfTrainers;
        this.priceList = priceList;
    }

    public CourseTemplate() {
        super();
    }

    public CourseTemplate(String acronym, int numberOfDates, String description, List<Qualification> requiredQualifications, int numberOfParticipants, int numberOfWaitList, int numberOfTrainers, List<CourseTemplatePrice> priceList) {
        super(acronym, numberOfDates, description, requiredQualifications);
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfWaitList = numberOfWaitList;
        this.numberOfTrainers = numberOfTrainers;
        this.priceList = priceList;
    }
}
