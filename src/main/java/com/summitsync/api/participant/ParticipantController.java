package com.summitsync.api.participant;

import com.summitsync.api.participant.dto.AddParticipantDto;
import com.summitsync.api.participant.dto.ParticipantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/participant")
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantMapper participantMapper;

    @PostMapping
    public ParticipantDto newParticipant(@RequestBody AddParticipantDto addParticipantDto, JwtAuthenticationToken jwt) {
        var participant = this.participantService.newParticipant(addParticipantDto);

        return this.participantMapper.mapParticipantToParticipantDto(participant);
    }

    @GetMapping("/{participantId}")
    public ParticipantDto getParticipantById(@PathVariable long participantId, JwtAuthenticationToken jwt) {
        var participant =  this.participantService.findById(participantId);

        return this.participantMapper.mapParticipantToParticipantDto(participant);
    }

    @DeleteMapping("/{participantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDataById(@PathVariable long participantId) {
        this.participantService.deleteParticipantById(participantId);
    }
}
