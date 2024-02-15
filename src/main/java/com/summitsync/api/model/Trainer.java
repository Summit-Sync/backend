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
public class Trainer {
    @Id
    @GeneratedValue
    private long tId;
    private String text;
    @ManyToMany
    private List<Qualification> qualifications;
}
