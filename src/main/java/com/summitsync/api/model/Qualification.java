package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
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
