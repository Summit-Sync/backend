package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventPeriod {
    @Id
    @GeneratedValue
    private long dId;
    private LocalDate date;
    private Time startTime;
    private Time endTime;
}
