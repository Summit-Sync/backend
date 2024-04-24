package com.summitsync.api.qualification.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddQualificationDto {
    @NotBlank(message = "qualification needs to have a name")
    private String name;
}
