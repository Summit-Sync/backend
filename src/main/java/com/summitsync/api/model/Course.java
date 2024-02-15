package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseId;
    private int number;
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;
    private String notes;
    private boolean visible;
    private BigDecimal actualPrice; // in euros
    private int numberOfParticipants;
    private int numberOfWaitList;
    private int numberOfTrainers;
    private int numberOfDates;
    private String description;
    private String title;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Qualification> requiredQualifications;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Participant> participants;
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTemplate template;
}
