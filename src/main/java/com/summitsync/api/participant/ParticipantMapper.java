package com.summitsync.api.participant;

import com.summitsync.api.keycloak.dto.KeycloakAddUserRequest;
import com.summitsync.api.keycloak.dto.KeycloakUser;
import com.summitsync.api.participant.dto.AddParticipantDto;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.participantstatus.dto.ParticipantStatusDto;
import com.summitsync.api.status.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class ParticipantMapper {
    public KeycloakAddUserRequest mapAddParticipantDtoToKeycloakAddUserRequest(AddParticipantDto addParticipantDto) {

        var username = addParticipantDto.getFirstName() + "." + addParticipantDto.getName();

        return KeycloakAddUserRequest.builder()
                .firstName(addParticipantDto.getFirstName())
                .lastName(addParticipantDto.getName())
                .enabled(true)
                .username(username)
                .email(addParticipantDto.getEmail())
                .build();
    }

    public ParticipantDto mapKeycloakUserToParticipantDto(KeycloakUser keycloakUser, Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getParticipantId())
                .email(keycloakUser.getEmail())
                .firstName(keycloakUser.getFirstName())
                .name(keycloakUser.getLastName())
                .status(participant.getStatus().getText())
                .build();
    }
}
