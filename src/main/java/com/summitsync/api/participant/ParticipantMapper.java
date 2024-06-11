package com.summitsync.api.participant;

import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.keycloak.dto.KeycloakAddUserRequest;
import com.summitsync.api.keycloak.dto.KeycloakUser;
import com.summitsync.api.participant.dto.AddParticipantDto;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.status.Status;
import com.summitsync.api.status.StatusMapper;
import com.summitsync.api.status.dto.StatusPostDto;
import com.summitsync.api.trainer.dto.AddTrainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class ParticipantMapper {
    private final KeycloakRestService keycloakRestService;
    private final StatusMapper statusMapper;
    public ParticipantDto mapParticipantToParticipantDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getParticipantId())
                .name(participant.getName())
                .firstName(participant.getFirstName())
                .status(this.statusMapper.mapStatusToStatusGetDto(participant.getStatus()))
                .email(participant.getEmail())
                .phone(participant.getPhone())
                .build();
    }

    public Participant mapAddParticipantDtoToParticipant(AddParticipantDto addParticipantDto, Status status) {
        return Participant.builder()
                .status(status)
                .phone(addParticipantDto.getPhone())
                .name(addParticipantDto.getName())
                .firstName(addParticipantDto.getFirstName())
                .email(addParticipantDto.getEmail())
                .build();
    }
}
