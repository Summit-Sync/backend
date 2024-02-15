package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CourseTrainer")
public class CourseTrainer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseTrainerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_tId")
    private Trainer trainer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_cId")
    private Course course;
    private boolean approved;
}
