package com.summitsync.api.participant;

import com.summitsync.api.course.Course;
import com.summitsync.api.status.Status;
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
@Table(name = "SS_Participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long participantId;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "participants")
    private Set<Course> courses;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "waitList")
    private List<Course> coursesWaitList;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Status status;
    private String phone;
    private String name;
    private String firstName;
    private String email;
}
