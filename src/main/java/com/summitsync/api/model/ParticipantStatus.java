package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_ParticipantStatus")
public class ParticipantStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long participantStatusId;
    private String status;
}
