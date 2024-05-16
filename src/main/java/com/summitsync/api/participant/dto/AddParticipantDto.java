package com.summitsync.api.participant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddParticipantDto {
    String name;
    String firstName;
    long status;
    String email;
}
