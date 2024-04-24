package com.summitsync.api.trainer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddTrainerDto {
    @NotBlank(message = "username can not be empty")
    String username;
    @NotBlank(message = "firstname can not be empty")
    String firstName;
    @NotBlank(message = "lastname can not be empty")
    String lastName;
    @NotBlank(message = "password can not be empty")
    String password;
    @NotBlank(message = "email can not be empty")
    String email;
    String phone;
}
