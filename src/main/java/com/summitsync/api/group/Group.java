package com.summitsync.api.group;

import com.summitsync.api.contact.Contact;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.location.Location;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainerapplication.TrainerApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private String title;
    private String description;
    private int numberOfDates;
    private int duration;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Contact contact;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventDate> dates;
    private int numberParticipants;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Location location;
    private String meetingPoint;
    private BigDecimal trainerPricePerHour;
    private BigDecimal pricePerParticipant;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_group_qualification_join",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "qualificationId")
    )
    private List<Qualification> qualifications;
    private int participantsPerTrainer;
    private String acronym;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_group_trainer_join",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "trainerId")
    )
    private List<Trainer> trainers;
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<TrainerApplication> applications;
}
