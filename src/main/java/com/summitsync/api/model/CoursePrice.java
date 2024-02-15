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
@Table(name = "SS_CoursePrice")
public class CoursePrice {
    @Id
    @GeneratedValue
    private long cprId;
    @OneToMany
    private List<Course> course;
    private String category;
    private double price;
}
