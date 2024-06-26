package com.summitsync.api.participant;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.participant.dto.AddParticipantDto;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.status.Status;
import com.summitsync.api.status.StatusMapper;
import com.summitsync.api.status.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final StatusService statusService;
    private final StatusMapper statusMapper;
    private final KeycloakRestService keycloakRestService;
    private final ParticipantMapper participantMapper;

    public Participant findById(long id) {
        var participant = this.participantRepository.findById(id);

        if (participant.isEmpty()) {
            throw new ResourceNotFoundException("participant with id " + id + " could not be found");
        }

        return participant.get();
    }

    public ParticipantDto getParticipant(long id, String jwt) {
        var participant = this.findById(id);
        var keycloakParticipant = this.keycloakRestService.getUser(participant.getSubjectId(), jwt);

        return this.participantMapper.mapKeycloakUserToParticipantDto(keycloakParticipant, participant);
    }

    public ParticipantDto newParticipant(AddParticipantDto addParticipantDto, String jwt) {
        var newStatus = Status.builder().text(addParticipantDto.getStatus().getText()).build();
        var status = this.statusService.saveStatus(newStatus);

        var keycloakAddUserResponse = this.keycloakRestService.addAndRetrieveUser(
                this.participantMapper.mapAddParticipantDtoToKeycloakAddUserRequest(addParticipantDto, String.format("%s.%s", addParticipantDto.getFirstName(), addParticipantDto.getName())),
                jwt
        );

        var participant = new Participant();
        participant.setSubjectId(keycloakAddUserResponse.getId());
        participant.setCourses(new HashSet<>());
        participant.setStatus(status);
        participant.setPhone(addParticipantDto.getPhone());

        var dbParticipant = this.participantRepository.save(participant);

        return this.participantMapper.mapKeycloakUserToParticipantDto(keycloakAddUserResponse, dbParticipant);
    }

    public void deleteParticipantById(long id, String jwt) {
        var participant = this.findById(id);

        this.keycloakRestService.deleteUser(participant.getSubjectId(), jwt);
        this.participantRepository.delete(participant);
    }


    public Participant getParticipantAndUpdate(ParticipantDto participantDto, String jwt) {
        var participant = this.findById(participantDto.getId());

        if (!participantDto.getStatus().getText().equals(participant.getStatus().getText())) {
            var status = Status.builder().text(participantDto.getStatus().getText()).build();
            var dbStatus = this.statusService.saveStatus(status);
            participant.setStatus(dbStatus);
        }

        if (participantDto.getPhone() != null && !participantDto.getPhone().equals(participant.getPhone())) {
            participant.setPhone(participantDto.getPhone());
        }
        var keycloakUser = this.keycloakRestService.getUser(participant.getSubjectId(), jwt);
        var addTrainer = this.participantMapper.mapParticipantDtoToAddDto(participantDto);
        var keycloakAddRequest = this.participantMapper.mapAddParticipantDtoToKeycloakAddUserRequestUpdate(addTrainer, keycloakUser.getEmail());

        this.keycloakRestService.updateUser(participant.getSubjectId(), keycloakAddRequest, jwt);
        return this.participantRepository.save(participant);
    }
}
