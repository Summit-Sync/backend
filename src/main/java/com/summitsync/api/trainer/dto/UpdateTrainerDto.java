package com.summitsync.api.trainer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTrainerDto {
    @NotBlank(message = "firstname can not be empty")
    String firstName;
    @NotBlank(message = "firstname can not be empty")
    String lastName;
    @NotBlank(message = "email can not be empty")
    String email;
    String phone;
}
