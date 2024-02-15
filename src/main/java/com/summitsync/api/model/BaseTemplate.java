package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseTemplate {
    @Id
    @GeneratedValue
    private long btId;
    private String acronym;
    private int numberOfDates;
    private String description;
    @ManyToMany
    private List<Qualification> requiredQualifications;
}
