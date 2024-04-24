package com.summitsync.api;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

@Component
public class MockMVCApiKey implements MockMvcBuilderCustomizer {
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        var restClient = RestClient.create();
        var res = restClient.post()
                .uri("https://keycloak.summitsync.meschter.me/realms/summit-sync/protocol/openid-connect/token")
                .body("grant_type=password&client_id=summit-sync-bff&client_secret=Wnhhsz4xp4k3PXUjrIyCSqhCLLHcjux1&username=test_admin&password=test&scope=openid+profile+email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(HashMap.class);

        var apiKeyRequestBuilder = MockMvcRequestBuilders
                .get("/")
                .header("Authorization", "Bearer " + res.get("access_token"));

        builder.defaultRequest(apiKeyRequestBuilder);
    }
}
