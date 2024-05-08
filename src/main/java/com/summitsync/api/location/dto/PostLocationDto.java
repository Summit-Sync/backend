package com.summitsync.api.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLocationDto {
    @NotBlank(message = "Street can not be empty")
    private String street;
    @NotBlank(message = "Postcode can not be empty")
    private String postCode;
    private String country;
    private String email;
    private String phone;
    private String mapsUrl;
    @NotBlank(message = "Title can not be empty")
    private String title;
    @NotBlank(message = "Title can not be empty")
    private String city;
}
