package com.summitsync.api.keycloak.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakResetPasswordRequest {
    @Builder.Default
    boolean temporary = true;
    @Builder.Default
    String type = "password";
    String value;
}
