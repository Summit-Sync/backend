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

    public Participant newParticipant(AddParticipantDto addParticipantDto) {
        var status = new Status();
        status.setText(addParticipantDto.getStatus().getText());
        status = this.statusService.saveStatus(status);

        return this.participantRepository.save(this.participantMapper.mapAddParticipantDtoToParticipant(addParticipantDto, status));
    }

    public void deleteParticipantById(long id) {
        var participant = this.findById(id);

        this.participantRepository.delete(participant);
    }


}
