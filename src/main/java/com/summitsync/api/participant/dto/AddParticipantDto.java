package com.summitsync.api.participant.dto;

import com.summitsync.api.status.dto.StatusGetDto;
import com.summitsync.api.status.dto.StatusPostDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddParticipantDto {
    String name;
    String firstName;
    StatusGetDto status;
    String email;
}
