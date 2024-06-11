package com.summitsync.api.course;

import com.summitsync.api.calendar.CalendarEvent;
import com.summitsync.api.calendar.CalendarEventDateSet;
import com.summitsync.api.coursetrainer.CourseTrainer;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.price.Price;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import com.summitsync.api.trainerapplication.TrainerApplication;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "SS_Course")
public class Course implements CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseId;
    private boolean visible;
    private boolean cancelled;
    private boolean finished;
    private String courseNumber;
    private String acronym;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventDate> dates;
    private int duration;
    private int numberParticipants;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ss_course_participants_join",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "participantId")
    )
    private List<Participant> participants;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "ss_course_waitList_join",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "participantId")
    )
    private List<Participant> waitList;
    private int numberWaitlist;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "ss_course_coursePrices_join",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "courseTemplatePriceId")
    )
    private List<Price> coursePrices;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;
    private String meetingPoint;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_course_requiredQualifications_join",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "qualificationId")
    )
    private List<Qualification> requiredQualifications;
    private int numberTrainer;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "ss_course_trainer_join",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "trainerId")
    )
    private List<Trainer> trainers;
    private String notes;
    private String title;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<CourseTrainer> courseTrainers;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<TrainerApplication> applications;

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
                    .map(trainer -> {
                        return keycloakRestService.getUser(trainer.getSubjectId(), keycloakRestService.getJwt()).getFirstName();
                    })
                    .toList());
        }
        var acronym = "Kurs";
        if (this.acronym != null) {
            acronym = this.acronym;
        }
        return acronym.toUpperCase() + this.courseNumber + " " + trainerNames;
    }

    @Override
    public long eventId() {
        return this.courseId;
    }

    @Override
    public String eventIdPrefix() {
        return "c";
    }

    @Override
    public String eventLocation() {
        if (this.meetingPoint == null)
            return "";

        return this.meetingPoint;
    }

    @Override
    public String eventColorId() {
        return "1";
    }
}


// CourseDTO: id (long), visible (boolean), canceled (boolean), finished (boolean), courseNumber (calculated String), acronym (string),
// description (String), dates (array von Date als String), duration(int), numberParticipants(int), participants (Array von ParticipantDTOs),
// waitList (Array of ParticipantDTOs), numberWaitlist(int), prices (PriceDto), location LocationDTO, meetingPoint (String),
// requiredQulaifications (Liste von QualifiationDTOs), numberTrainers (int), trainerList (Array of TrainerDTOs), notes (String)
