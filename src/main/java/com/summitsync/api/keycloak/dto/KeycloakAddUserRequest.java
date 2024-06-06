package com.summitsync.api.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class KeycloakAddUserRequest {
    String username;
    boolean enabled;
    String firstName;
    String lastName;
    List<String> groups;
    String email;
    Map<String, Object> attributes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> requiredActions;
}
