package com.summitsync.api.bff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summitsync.api.JwtAuthenticationConverterRoles;
import com.summitsync.api.exceptionhandler.CodeExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
public class SessionService {
    @Value("${summitsync.bff.authorization-url}")
    private String authorizationUrl;

    @Value("${summitsync.bff.token-endpoint}")
    private String tokenUrl;

    @Value("${summitsync.bff.client-id}")
    private String clientID;

    @Value("${summitsync.bff.redirect-uri}")
    private String redirectUri;

    @Value("${summitsync.bff.client-secret}")
    private String clientSecret;

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    private final SecureRandom secureRandom;
    private final AuthStateRepository authStateRepository;
    private final SessionRepository sessionRepository;
    private final NimbusJwtDecoder jwtDecoder;
    public SessionService(AuthStateRepository authStateRepository, SessionRepository sessionRepository, @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String issuerUri) {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
        this.secureRandom = new SecureRandom();
        this.authStateRepository = authStateRepository;
        this.sessionRepository = sessionRepository;
        this.jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    private String getRandomString(int length) {
        var randomBytes = new byte[length];
        this.secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashCodeVerifier(String codeVerifier) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            md.update(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            var result = md.digest();

            return Base64.getUrlEncoder().withoutPadding().encodeToString(result);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAuthorizationUrl() {
        var state = this.getRandomString(16);

        var codeVerifier = this.getRandomString(32);
        System.out.println("CODE VERIFIER: " + codeVerifier);
        var hashedCodeVerifier = this.hashCodeVerifier(codeVerifier);
        System.out.println("CODE VERIFIER hashed: " + hashedCodeVerifier);
        var authState = new AuthState(state, this.redirectUri, codeVerifier);
        this.authStateRepository.save(authState);

        return UriComponentsBuilder.fromHttpUrl(this.authorizationUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", this.clientID)
                .queryParam("redirect_uri", this.redirectUri)
                .queryParam("scope", "openid profile email")
                .queryParam("state", state)
                .queryParam("code_challenge", hashedCodeVerifier)
                .queryParam("code_challenge_method", "S256")
                .toUriString();
    }

    private String getBasicCredentials() {
        var colonSeperated = this.clientID + ":" + this.clientSecret;
        var colonSeperatedBytes = colonSeperated.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(colonSeperatedBytes);
    }
    public AccessTokenResponseDto accessTokenRequest(String code, String state) {
        var authStateOptional = this.authStateRepository.findById(state);
        var authState = authStateOptional.orElseThrow(() -> new CodeExchangeException("invalid state", HttpStatus.BAD_REQUEST));
        System.out.println("Code verifier in callback: " + authState.getCodeVerifier());
        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", code);
        requestBody.add("redirect_uri", authState.getRedirectUri());
        requestBody.add("client_id", clientID);
        requestBody.add("code_verifier", authState.getCodeVerifier());

        this.authStateRepository.delete(authState);

        return this.restClient
                .post()
                .uri(this.tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + this.getBasicCredentials())
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                        AccessTokenErrorResponseDto accessTokenErrorResponseDto =
                                objectMapper.readValue(response.getBody(), AccessTokenErrorResponseDto.class);

                        throw new CodeExchangeException(accessTokenErrorResponseDto.getError() + " (" + accessTokenErrorResponseDto.getError_description() + ")", HttpStatus.BAD_REQUEST);
                    } else {
                        throw new CodeExchangeException("server error", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }))
                .body(AccessTokenResponseDto.class);
    }

    public String newSession(AccessTokenResponseDto accessTokenResponseDto) {
        var sessionId = this.getRandomString(32);
        var role = JwtAuthenticationConverterRoles.extractRolesFromJwt(this.jwtDecoder.decode(accessTokenResponseDto.getAccessToken())).getFirst();
        var session = SessionMapper.mapAccessTokenResponseDtoToSession(sessionId, accessTokenResponseDto, role);
        this.sessionRepository.save(session);

        return sessionId;
    }

    public Session updateSession(Session session, AccessTokenResponseDto accessTokenResponseDto) {
        session.setAccessToken(accessTokenResponseDto.getAccessToken());
        session.setExpiresIn(accessTokenResponseDto.getExpiresIn());
        session.setRefreshExpiresIn(accessTokenResponseDto.getRefreshExpiresIn());
        session.setRefreshToken(accessTokenResponseDto.getRefreshToken());
        session.setIdToken(accessTokenResponseDto.getIdToken());
        session.setUpdated(Instant.now().atZone(ZoneOffset.UTC));

        return this.sessionRepository.save(session);

    }

    public AccessTokenResponseDto refreshToken(String refreshToken) {
        System.out.println("refreshing token");
        System.out.println(refreshToken);
        // FIXME: can probably deduplicate some code here with other http requests to keycloak
        var body = new LinkedMultiValueMap<String, String>();
        body.add("client_id", clientID);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        return this.restClient
                .post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(AccessTokenResponseDto.class);
    }
}
