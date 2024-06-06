package com.summitsync.api.trainerapplication;

import com.summitsync.api.course.Course;
import com.summitsync.api.group.Group;
import com.summitsync.api.trainer.Trainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_TrainerApplication")
public class TrainerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long applicationId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Trainer trainer;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Group group;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Course course;
    private AcceptStatus accepted;
}
