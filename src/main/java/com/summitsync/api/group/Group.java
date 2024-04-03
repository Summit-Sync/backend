package com.summitsync.api.group;

import com.summitsync.api.contact.Contact;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long groupId;
    @OneToMany(fetch = FetchType.LAZY)
    private List<EventDate> period;
    private Integer numberOfParticipants;
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;
    private String notes;
    private BigDecimal pricePerParticipant;
    private BigDecimal totalPrice;
    private int numberOfDates;
    private String description;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contact contact;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Qualification> requiredQualifications;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupTemplate template;
}
