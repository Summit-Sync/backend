package com.summitsync.api.trainer;

import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long trainerId;
    private String subjectId;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Qualification> qualifications = new HashSet<>();
}
