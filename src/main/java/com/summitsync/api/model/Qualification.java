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
@Table(name = "SS_Qualification")
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long qualificationId;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Trainer> trainers;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> courses;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Group> groups;
}
