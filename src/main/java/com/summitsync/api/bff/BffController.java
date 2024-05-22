package com.summitsync.api.bff;

import com.summitsync.api.exceptionhandler.InvalidSessionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class BffController {

    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    private long getDurationRemaining(LocalDateTime created, long expiresIn) {
        var now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        var sessionExpiresAt = created.toEpochSecond(ZoneOffset.UTC) + expiresIn;
        return Math.max(0, sessionExpiresAt - now);
    }

    @GetMapping("/access_token")
    public ResponseEntity<BffAccessTokenResponse> getAccessToken(@CookieValue("SESSION_ID") String sessionId) {
        var sessionOptional = this.sessionRepository.findById(sessionId);
        var session = sessionOptional.orElseThrow(InvalidSessionException::new);
        var created = session.getCreated();

        var refreshRemaining = this.getDurationRemaining(created, session.getRefreshExpiresIn());
        var accessTokenRemaining = this.getDurationRemaining(created, session.getExpiresIn());

        if (refreshRemaining <= 10) {
            throw new InvalidSessionException();
        }

        if (accessTokenRemaining <= 10) {
            var accessTokenResponse = this.sessionService.refreshToken(session.getRefreshToken());
            session = this.sessionService.updateSession(session, accessTokenResponse);
        }

        var body = new BffAccessTokenResponse(session.getAccessToken(),
                refreshRemaining,
                accessTokenRemaining,
                session.getRole());
        return new ResponseEntity<>(body, HttpStatus.OK);
   }
}
