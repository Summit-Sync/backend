package com.summitsync.api.keycloak.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KeycloakUser {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    Map<String, List<String>> attributes;
    boolean totp;
    boolean enabled;
    long createdTimestamp;
    boolean emailVerified;
}
