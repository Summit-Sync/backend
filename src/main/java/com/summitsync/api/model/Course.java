package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {
    @Id
    @GeneratedValue
    private long cId;
    private int number;
    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;
    private String notes;
    private boolean visible;
    private double actualPrice; // in euros
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    private int numberOfDates;
    private String description;
    @ManyToMany
    private List<Qualification> requiredQualifications;
    @ManyToMany
    private List<Participant> participants;
    @ManyToOne
    private CourseTemplate template;
}
