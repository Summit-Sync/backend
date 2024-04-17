package com.summitsync.api.trainerapplication.dto;

import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Data;

import java.util.List;

@Data
public class TrainerApplicationDto {
    long id;
    boolean accepted;
    String firstName;
    String lastName;
    String email;
    String phone;
    List<QualificationDto> qualificationDTOs;
    String subjectId;
}
