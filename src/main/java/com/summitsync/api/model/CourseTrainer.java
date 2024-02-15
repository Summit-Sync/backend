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
    @GeneratedValue
    private long ctrId;
    @ManyToOne
    @JoinColumn(name = "trainer_tId")
    private Trainer trainer;
    @ManyToOne
    @JoinColumn(name = "course_cId")
    private Course course;
    private boolean approved;
}
