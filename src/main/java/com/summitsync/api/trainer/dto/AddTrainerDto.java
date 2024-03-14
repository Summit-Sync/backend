package com.summitsync.api.trainer.dto;

import lombok.Data;

@Data
public class AddTrainerDto {
    String username;
    String firstName;
    String lastName;
    String password;
    String email;
    String phone;
}
