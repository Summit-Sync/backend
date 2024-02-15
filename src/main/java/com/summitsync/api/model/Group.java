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
@Table(name = "SS_Group")
public class Group {
    @Id
    @GeneratedValue
    private long gId;
    @OneToMany
    private List<EventPeriod> period;
    private int numberOfParticipants;
    @ManyToOne
    private Location location;
    private String notes;
    private double pricePerParticipant;
    private double totalPrice;
    private int numberOfDates;
    private String description;
    @ManyToMany
    private List<Qualification> requiredQualifications;
    @ManyToOne
    private GroupTemplate template;

}
