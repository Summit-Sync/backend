package com.summitsync.api.keycloak;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.summitsync.api.exceptionhandler.KeycloakApiException;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.keycloak.dto.KeycloakAddUserRequest;
import com.summitsync.api.keycloak.dto.KeycloakResetPasswordRequest;
import com.summitsync.api.keycloak.dto.KeycloakUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class KeycloakRestService {
    @Value("${summitsync.keycloak.rest-endpoint}")
    private String apiBase;

    private final RestClient restClient;

    public KeycloakRestService() {
        this.restClient = RestClient.create();
    }

    private <B> RestClient.ResponseSpec rawRequest(HttpMethod method, String uri, String jwt, B body) {
        System.out.println(uri);
        System.out.println(jwt);
        var preparedRequest =  this.restClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt);

        if (body != null) {
            preparedRequest.body(body);
        }

       var kcResponse = preparedRequest.retrieve();
        kcResponse.onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
            if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
               throw new KeycloakApiException("resource not found", (HttpStatus) response.getStatusCode());
            }
            var objectMapper = new ObjectMapper();
            Map<String, Object> errorMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            String errorMessage = (String) errorMap.getOrDefault("errorMessage", "unknown keycloak error");

            throw new KeycloakApiException(errorMessage, (HttpStatus) response.getStatusCode());
        }));

        return kcResponse;
    }

    public <B, R> R request(HttpMethod method, String uri, String jwt, B body, Class<R> responseType) {
        return this.rawRequest(method, uri, jwt, body).body(responseType);
    }

    public void setUserPassword(KeycloakResetPasswordRequest resetPasswordRequest, String subjectId, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("users", subjectId, "reset-password").toUriString();
        this.rawRequest(HttpMethod.PUT, uri, jwt, resetPasswordRequest).toBodilessEntity();
    }

    public KeycloakUser addAndRetrieveUser(KeycloakAddUserRequest addUserRequest, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("users").toUriString();

        var response = this.rawRequest(HttpMethod.POST, uri, jwt, addUserRequest).toBodilessEntity();
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)) {
            throw new KeycloakApiException("expected status code 201, but keycloak responded with " + response.getStatusCode(), (HttpStatus) response.getStatusCode());
        }

        var locationHeader = response.getHeaders().getLocation();
        if (locationHeader == null) {
            throw new KeycloakApiException("kc didn't set location header, despite 201", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.request(HttpMethod.GET, locationHeader.toASCIIString(), jwt, null, KeycloakUser.class);
    }

    public KeycloakUser getUser(String subjectId, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("users", subjectId).toUriString();

        return this.request(HttpMethod.GET, uri, jwt, null, KeycloakUser.class);
    }

    public void deleteUser(String subjectId, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("users", subjectId).toUriString();

        var response = this.rawRequest(HttpMethod.DELETE, uri, jwt, null).toBodilessEntity();

        if (!response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            throw new KeycloakApiException("expected status code 201 from keycloak but got " + response.getStatusCode(), (HttpStatus) response.getStatusCode());
        }
    }

    public KeycloakUser updateUser(String subjectId, KeycloakAddUserRequest keycloakAddUserRequest, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("users", subjectId).toUriString();

        var response = this.rawRequest(HttpMethod.PUT, uri, jwt, keycloakAddUserRequest).toBodilessEntity();

        if (!response.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT)) {
            throw new KeycloakApiException("expected status code 201 from keycloak but got " + response.getStatusCode(), (HttpStatus) response.getStatusCode());
        }

        return this.getUser(subjectId, jwt);
    }

    public String getGroupIdByName(String groupName, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("groups").toUriString();
        var response = this.request(HttpMethod.GET, uri, jwt, null, ArrayList.class);

        for (var entry: response) {
            var mapEntry = (HashMap<String, String>) entry;
            if (mapEntry.get("name").equals(groupName)) {
                return mapEntry.get("id");
            }
        }

        throw new ResourceNotFoundException("no group with name " + groupName + " found");
    }

    public List<KeycloakUser> getAllMembersOfGroup(String groupId, String jwt) {
        var uri = UriComponentsBuilder.fromUriString(apiBase).pathSegment("groups", groupId, "members").toUriString();
        return Arrays.stream(this.request(HttpMethod.GET, uri, jwt, null, KeycloakUser[].class)).toList();
    }

}
