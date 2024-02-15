package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long contactId;
    private String name;
    private String email;
}
