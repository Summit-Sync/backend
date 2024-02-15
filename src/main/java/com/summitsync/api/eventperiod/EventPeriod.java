package com.summitsync.api.eventperiod;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_EventPeriod")
public class EventPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long eventPeriodId;
    private LocalDate date;
    private Time startTime;
    private Time endTime;
}
