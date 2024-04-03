package com.summitsync.api.participant.dto;

import com.summitsync.api.course.Course;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ParticipantDto {
    long id;
    String name;
    String firstName;
    String status;
    String email;
}
