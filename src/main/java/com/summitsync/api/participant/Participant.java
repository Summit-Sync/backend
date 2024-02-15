package com.summitsync.api.participant;

import com.summitsync.api.course.Course;
import com.summitsync.api.participantstatus.ParticipantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long participantId;
    private String name;
    private String firstName;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ParticipantStatus status;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> courses;
}
