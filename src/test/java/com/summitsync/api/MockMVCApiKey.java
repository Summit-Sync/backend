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
    public static String getAccessToken() {
        var restClient = RestClient.create();
        var res = restClient.post()
                .uri("https://keycloak.summitsync.meschter.me/realms/summit-sync/protocol/openid-connect/token")
                .body("grant_type=password&client_id=summit-sync-bff&client_secret=Wnhhsz4xp4k3PXUjrIyCSqhCLLHcjux1&username=test_admin&password=test&scope=openid+profile+email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(HashMap.class);

        return (String) res.get("access_token");
    }
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {

        var apiKeyRequestBuilder = MockMvcRequestBuilders
                .get("/")
                .header("Authorization", "Bearer " + MockMVCApiKey.getAccessToken());

        builder.defaultRequest(apiKeyRequestBuilder);
    }
}
