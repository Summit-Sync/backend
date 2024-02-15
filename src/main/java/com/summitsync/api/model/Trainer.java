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
@Table(name = "SS_Trainer")
public class Trainer {
    @Id
    @GeneratedValue
    private long tId;
    private String text;
    @ManyToMany
    private List<Qualification> qualifications;
}
