package com.summitsync.api.trainer.dto;

import com.summitsync.api.qualification.dto.QualificationDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    List<QualificationDto> qualifications;
}
