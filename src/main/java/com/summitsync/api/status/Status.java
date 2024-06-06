package com.summitsync.api.status;

import com.summitsync.api.coursetrainer.CourseTrainer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Table(name = "SS_Status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long statusId;
    String text;
    @OneToOne(mappedBy = "status", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private CourseTrainer courseTrainer;
}
