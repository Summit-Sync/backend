package com.summitsync.api.grouptemplate;

import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "SS_GroupTemplate")
public class GroupTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long groupTemplateId;
    private String acronym;
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Location location;
    private String meetingPoint;
    private BigDecimal trainerPricerPerHour;
    private BigDecimal pricePerParticipant;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "ss_grouptemplate_trainer_join",
            joinColumns = @JoinColumn(name = "groupTemplateId"),
            inverseJoinColumns = @JoinColumn(name = "trainerId")
    )
    private Set<Qualification> requiredQualifications;
    private int participantsPerTrainer;

}
