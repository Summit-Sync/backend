package com.summitsync.api.bff;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/auth")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/authorization_url")
    public String RedirectToAuthorizationUrl() {
        return "redirect:" + this.sessionService.getAuthorizationUrl();
    }

    @GetMapping("/callback")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void AuthCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) {
        var accessTokenResponse = this.sessionService.accessTokenRequest(code, state);
        var sessionId = this.sessionService.newSession(accessTokenResponse);
        var sessionIdCookie = new Cookie("SESSION_ID", sessionId);
        sessionIdCookie.setPath("/");
        sessionIdCookie.setSecure(true);
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setAttribute("Same-Site", "strict");

        response.addCookie(sessionIdCookie);
    }
}
