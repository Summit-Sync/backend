package com.summitsync.api.group;

import com.summitsync.api.calendar.CalendarEvent;
import com.summitsync.api.calendar.CalendarEventDateSet;
import com.summitsync.api.contact.Contact;
import com.summitsync.api.course.CourseMapper;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.keycloak.KeycloakRestService;
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
public class Group implements CalendarEvent {
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
    @OneToMany(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER)
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
    private String notes;

    @Override
    public List<CalendarEventDateSet> occurrences() {
        return CourseMapper.mapEventDatesListToCalendarEventDateSetList(this.dates);
    }

    @Override
    public String eventDescription(CalendarEventDateSet calendarEventDateSet) {
        return this.notes;
    }

    @Override
    public String eventTitle(CalendarEventDateSet calendarEventDateSet, KeycloakRestService keycloakRestService) {
        String trainerNames = "";
        if (!this.trainers.isEmpty()) {
            trainerNames = String.join(" ", this.trainers.stream()
                    .map(trainer -> keycloakRestService.getUser(trainer.getSubjectId(), keycloakRestService.getJwt()).getFirstName())
                    .toList());
        }

        var acronym = "Gruppe";
        if (this.acronym != null) {
            acronym = this.acronym;
        }
        return acronym.toUpperCase() + this.groupNumber + " " + trainerNames;
    }

    @Override
    public long eventId() {
        return this.groupId;
    }

    @Override
    public String eventIdPrefix() {
        return "g";
    }

    @Override
    public String eventLocation() {
        if (this.meetingPoint == null)
            return "";

        return this.meetingPoint;
    }

    @Override
    public String eventColorId() {
        return "2";
    }
}
