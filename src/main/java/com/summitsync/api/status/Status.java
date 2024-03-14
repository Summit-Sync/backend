package com.summitsync.api.status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "SS_Status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long statusId;
    String text;
}
