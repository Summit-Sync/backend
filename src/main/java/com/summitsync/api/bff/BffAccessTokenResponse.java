package com.summitsync.api.bff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BffAccessTokenResponse {
    String access_token;
    long expires_in;
}
