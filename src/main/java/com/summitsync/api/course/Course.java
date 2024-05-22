package com.summitsync.api.course;

import com.summitsync.api.coursetrainer.CourseTrainer;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.price.Price;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainerapplication.TrainerApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "SS_Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseId;
    private boolean visible;
    private boolean cancelled;
    private boolean finished;
    private String courseNumber;
    private String acronym;
    private String description;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private Set<EventDate> dates;
    private int duration;
    private int numberParticipants;
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Participant> participants;
    @ManyToMany(mappedBy = "coursesWaitList", cascade = CascadeType.PERSIST)
    private Set<Participant> waitList;
    private int numberWaitlist;
    @ManyToMany(mappedBy = "courses", cascade = CascadeType.PERSIST)
    private Set<Price> coursePrices;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;
    private String meetingPoint;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "courses")
    private Set<Qualification> requiredQualifications;
    private int numberTrainer;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "courses")
    private Set<Trainer> trainers;
    private String notes;
    private String title;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<CourseTrainer> courseTrainers;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<TrainerApplication> applications;
}


// CourseDTO: id (long), visible (boolean), canceled (boolean), finished (boolean), courseNumber (calculated String), acronym (string),
// description (String), dates (array von Date als String), duration(int), numberParticipants(int), participants (Array von ParticipantDTOs),
// waitList (Array of ParticipantDTOs), numberWaitlist(int), prices (PriceDto), location LocationDTO, meetingPoint (String),
// requiredQulaifications (Liste von QualifiationDTOs), numberTrainers (int), trainerList (Array of TrainerDTOs), notes (String)
