package com.summitsync.api.date;

import com.summitsync.api.course.Course;
import com.summitsync.api.group.Group;
import jakarta.persistence.*;
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
    private LocalDateTime startTime;
    private int durationInMinutes;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Course course;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Group group;
}
