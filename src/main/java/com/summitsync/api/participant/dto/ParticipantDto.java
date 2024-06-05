package com.summitsync.api.participant.dto;

import com.summitsync.api.course.Course;
import com.summitsync.api.status.dto.StatusGetDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ParticipantDto {
    long id;
    String name;
    String firstName;
    StatusGetDto status;
    String email;
    String phone;
}
