package com.summitsync.api.bff;

import com.summitsync.api.exceptionhandler.InvalidSessionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class BffController {

    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    private long getDurationRemaining(ZonedDateTime created, long expiresIn) {
        assert created.getZone().equals(ZoneOffset.UTC);
        if (!created.getZone().equals(ZoneOffset.UTC)) {
            throw new RuntimeException("Zone is not UTC");
        }
        var now = Instant.now().getEpochSecond();
        var sessionExpiresAt = created.toEpochSecond() + expiresIn;
        return Math.max(0, sessionExpiresAt - now);
    }

    @GetMapping("/access_token")
    public ResponseEntity<BffAccessTokenResponse> getAccessToken(@CookieValue("SESSION_ID") String sessionId) {
        var sessionOptional = this.sessionRepository.findById(sessionId);
        var session = sessionOptional.orElseThrow(InvalidSessionException::new);
        var created = session.getCreated();
        var updated = session.getUpdated();

        var refreshRemaining = this.getDurationRemaining(created, session.getRefreshExpiresIn());
        var accessTokenRemaining = this.getDurationRemaining(updated, session.getExpiresIn());

        if (refreshRemaining <= 10) {
            throw new InvalidSessionException();
        }

        if (accessTokenRemaining <= 10) {
            var accessTokenResponse = this.sessionService.refreshToken(session.getRefreshToken());
            session = this.sessionService.updateSession(session, accessTokenResponse);
            accessTokenRemaining = this.getDurationRemaining(session.getUpdated(), session.getExpiresIn());
        }

        var body = new BffAccessTokenResponse(session.getAccessToken(),
                Instant.now().atZone(ZoneOffset.UTC).plusSeconds(accessTokenRemaining),
                Instant.now().atZone(ZoneOffset.UTC).plusSeconds(refreshRemaining),
                session.getRole());
        return new ResponseEntity<>(body, HttpStatus.OK);
   }
}
