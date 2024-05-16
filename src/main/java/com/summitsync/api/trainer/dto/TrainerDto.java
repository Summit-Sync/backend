package com.summitsync.api.trainer.dto;

import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainerDto {
    long id;
    String subjectId;
    String firstName;
    String lastName;
    List<QualificationDto> qualifications;
    String email;
    String phone;
}
