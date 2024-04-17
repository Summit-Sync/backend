package com.summitsync.api.trainer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTrainerDto {
    String lastName;
    String email;
    String phone;
}
