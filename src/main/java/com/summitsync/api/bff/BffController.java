package com.summitsync.api.bff;

import com.summitsync.api.exceptionhandler.InvalidSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class BffController {

    private final SessionRepository sessionRepository;

    public BffController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;    
    }
    @GetMapping("/access_token")
    public ResponseEntity<BffAccessTokenResponse> getAccessToken(@CookieValue("SESSION_ID") String sessionId) {
        var sessionOptional = this.sessionRepository.findById(sessionId);
        var session = sessionOptional.orElseThrow(InvalidSessionException::new);
        var body = new BffAccessTokenResponse(session.getAccessToken(), session.getExpiresIn());
        return new ResponseEntity<>(body, HttpStatus.OK);
   }
}
