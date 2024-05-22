package com.summitsync.api.participant;

import com.summitsync.api.course.Course;
import com.summitsync.api.status.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long participantId;
    private String subjectId;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> coursesWaitList;
    @OneToOne(fetch = FetchType.LAZY)
    private Status status;
}
