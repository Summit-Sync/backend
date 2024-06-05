package com.summitsync.api.participant;

import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.keycloak.dto.KeycloakAddUserRequest;
import com.summitsync.api.keycloak.dto.KeycloakUser;
import com.summitsync.api.participant.dto.AddParticipantDto;
import com.summitsync.api.participant.dto.ParticipantDto;
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
    public KeycloakAddUserRequest mapAddParticipantDtoToKeycloakAddUserRequestUpdate(AddParticipantDto addParticipantDto, String oldEmail) {
        var req = KeycloakAddUserRequest.builder()
                .firstName(addParticipantDto.getFirstName())
                .lastName(addParticipantDto.getName())
                .enabled(true);

        if (!oldEmail.equalsIgnoreCase(addParticipantDto.getEmail())) {
            req.email(addParticipantDto.getEmail());
        }

        return req.build();
    }
    public KeycloakAddUserRequest mapAddParticipantDtoToKeycloakAddUserRequest(AddParticipantDto addParticipantDto, String username) {

        var req = KeycloakAddUserRequest.builder()
                .firstName(addParticipantDto.getFirstName())
                .lastName(addParticipantDto.getName())
                .enabled(true)
                .email(addParticipantDto.getEmail());

        if (username != null) {
            req.username(username);
        }

        return req.build();

    }

    public ParticipantDto mapKeycloakUserToParticipantDto(KeycloakUser keycloakUser, Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getParticipantId())
                .email(keycloakUser.getEmail())
                .firstName(keycloakUser.getFirstName())
                .name(keycloakUser.getLastName())
                .status(statusMapper.mapStatusToStatusGetDto(participant.getStatus()))
                .phone(participant.getPhone())
                .build();
    }

    public ParticipantDto mapParticipantToParticipantDto(Participant participant, String jwt) {
        var keycloakUser = this.keycloakRestService.getUser(participant.getSubjectId(), jwt);

        return this.mapKeycloakUserToParticipantDto(keycloakUser, participant);
    }

    public AddParticipantDto mapParticipantDtoToAddDto(ParticipantDto participant) {
        return AddParticipantDto.builder()
                .email(participant.getEmail())
                .firstName(participant.getFirstName())
                .name(participant.getName())
                .status(participant.getStatus())
                .phone(participant.getPhone())
                .build();
    }
}
