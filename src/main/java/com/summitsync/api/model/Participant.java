package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Participant {
    @Id
    @GeneratedValue
    private long pId;
    private String name;
    private String firstName;
    @OneToOne
    private ParticipantStatus status;
    @ManyToMany
    private List<Course> courses;
}
