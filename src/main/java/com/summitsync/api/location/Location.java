package com.summitsync.api.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SS_Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long locationId;
    private String street;
    private String postCode;
    private String country;
    private String email;
    private String phone;
    private String mapsUrl;
    private String title;
    private String city;
}
