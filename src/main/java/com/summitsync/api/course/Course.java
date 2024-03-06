package com.summitsync.api.course;

import com.summitsync.api.courseprice.CoursePrice;
import com.summitsync.api.coursetemplate.CourseTemplate;
import com.summitsync.api.coursetrainer.CourseTrainer;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.location.Location;
import com.summitsync.api.participant.Participant;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.Trainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseId;
    private String acronym;
    private int number;
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;
    private String notes;
    private boolean visible;
    private boolean canceled;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CoursePrice>priceList;
    private Integer numberOfParticipants;
    private Integer lengthOfWaitList;
    private Integer numberOfTrainers;
    private Integer numberOfDates;
    private String description;
    private String title;
    private Integer duration;
    private BigDecimal actualPrice;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Qualification> requiredQualifications;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Participant> participants;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Participant> waitList;
    @OneToMany(fetch = FetchType.LAZY)
    private List<CourseTrainer>trainerList;
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTemplate template;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<EventDate>dates;
}
