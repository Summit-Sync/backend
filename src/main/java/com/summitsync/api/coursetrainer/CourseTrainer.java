package com.summitsync.api.coursetrainer;

import com.summitsync.api.course.Course;
import com.summitsync.api.status.Status;
import com.summitsync.api.trainer.Trainer;
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Trainer trainer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Course course;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Status status;
}
