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

    @PostMapping
    public ParticipantDto newParticipant(@RequestBody AddParticipantDto addParticipantDto, JwtAuthenticationToken jwt) {
        return this.participantService.newParticipant(addParticipantDto, jwt.getToken().getTokenValue());
    }

    @GetMapping("/{participantId}")
    public ParticipantDto getParticipantById(@PathVariable long participantId, JwtAuthenticationToken jwt) {
        return this.participantService.getParticipant(participantId, jwt.getToken().getTokenValue());
    }

    @DeleteMapping("/{participantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDataById(@PathVariable long participantId, JwtAuthenticationToken jwt) {
        this.participantService.deleteParticipantById(participantId, jwt.getToken().getTokenValue());
    }
}
