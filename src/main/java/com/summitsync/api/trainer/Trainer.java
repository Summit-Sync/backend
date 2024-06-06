package com.summitsync.api.trainer;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetrainer.CourseTrainer;
import com.summitsync.api.group.Group;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainerapplication.TrainerApplication;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "SS_Trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long trainerId;
    private String subjectId;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_trainer_qualification_join",
            joinColumns = @JoinColumn(name = "trainerid"),
            inverseJoinColumns = @JoinColumn(name = "qualificationId")
    )
    private List<Qualification> qualifications = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "trainers")
    private List<Course> courses;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<CourseTrainer> courseTrainers;
    @ManyToMany(mappedBy = "trainers", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Group> groups;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<TrainerApplication> trainerApplications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return subjectId.equals(trainer.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }
}
