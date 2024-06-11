package com.summitsync.api.date;

import com.summitsync.api.course.Course;
import com.summitsync.api.group.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SS_EventDate")
public class EventDate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long eventDateId;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private int durationInMinutes;
    @ManyToOne()
    private Course course;
    @ManyToOne()
    private Group group;
}
