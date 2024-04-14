package com.summitsync.api.coursetemplate;

import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SS_CourseTemplate")
public class CourseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseTemplateId;
    private String acronym;
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    private int numberParticipants;
    private int numberWaitlist;
    @OneToOne
    private Location location;
    private String meetingPoint;
    @OneToMany
    private Set<CourseTemplatePrice> courseTemplatePrices;
    @OneToMany
    private Set<Qualification> qualifications;
    private int numberTrainer;
}


// CourseTemplateDTO: id (long), acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location (LocationDTO), meetingPoint (String), price (array von priceDto),
// requiredQulaifications (array von QualificationDto), numberTrainers (int)
