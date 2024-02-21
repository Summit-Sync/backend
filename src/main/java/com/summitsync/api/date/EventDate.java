package com.summitsync.api.date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

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
    private LocalDateTime date;

}
