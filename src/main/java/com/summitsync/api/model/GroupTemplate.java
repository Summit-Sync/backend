package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_GroupTemplate")
public class GroupTemplate extends BaseTemplate {
    private double pricePerTrainerPerHour;
    private int trainerKey;
}
