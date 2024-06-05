package com.summitsync.api.coursetemplate;

import com.summitsync.api.price.Price;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Location location;
    private String meetingPoint;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_coursetemplate_price",
            joinColumns = @JoinColumn(name = "courseTemplateId"),
            inverseJoinColumns = @JoinColumn(name = "courseTemplatePriceId")
    )
    private List<Price> prices;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "ss_coursetemplate_qualifications",
            joinColumns = @JoinColumn(name = "courseTemplateId"),
            inverseJoinColumns = @JoinColumn(name = "qualificationId")
    )
    private List<Qualification> qualifications;
    private int numberTrainer;
}


// CourseTemplateDTO: id (long), acronym (string), title (string), description (string), numberOfDates (int), duration (int),
// numberParticipants (int), numberWaitlist (int), location (LocationDTO), meetingPoint (String), price (array von priceDto),
// requiredQulaifications (array von QualificationDto), numberTrainers (int)
