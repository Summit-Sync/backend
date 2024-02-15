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
    @GeneratedValue
    private long qId;
    private String name;
    @ManyToMany
    private List<Trainer> trainers;
    @ManyToMany
    private List<Course> courses;
    @ManyToMany
    private List<Group> groups;
}
