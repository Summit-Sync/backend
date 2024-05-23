package com.summitsync.api.group;

import com.summitsync.api.contact.Contact;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "SS_Group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long groupId;
    private String groupNumber;
    boolean cancelled;
    String title;
    String description;
    int numberOfDates;
    int duration;
    @OneToOne
    Contact contact;
    @OneToMany(cascade=CascadeType.ALL)
    Set<EventDate> dates;
    int numberParticipants;
    @ManyToOne
    Location location;
    String meetingPoint;
    BigDecimal trainerPricePerHour;
    BigDecimal pricePerParticipant;
    @ManyToMany
    Set<Qualification> qualifications;
    int participantsPerTrainer;
    @ManyToMany
    Set<Trainer> trainers;
    BigDecimal totalPrice;
    String acronym;
}
