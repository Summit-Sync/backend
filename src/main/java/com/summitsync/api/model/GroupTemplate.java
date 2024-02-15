package com.summitsync.api.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupTemplate extends BaseTemplate {
    private double pricePerTrainerPerHour;
    private int trainerKey;
}
