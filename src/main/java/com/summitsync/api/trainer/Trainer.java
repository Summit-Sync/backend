package com.summitsync.api.trainer;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetrainer.CourseTrainer;
import com.summitsync.api.group.Group;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainerapplication.TrainerApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long trainerId;
    private String subjectId;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Qualification> qualifications = new HashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "trainers")
    private List<Course> courses;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseTrainer> courseTrainers;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Group> groups;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerApplication> trainerApplications;
}
