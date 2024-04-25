package com.summitsync.api.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLocationDto {
    private String street;
    private String postCode;
    private String country;
    private String email;
    private String phone;
    private String mapsUrl;
    private String title;
    private String city;
}
