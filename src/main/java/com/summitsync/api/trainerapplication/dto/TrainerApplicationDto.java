package com.summitsync.api.trainerapplication.dto;

import com.summitsync.api.qualification.dto.QualificationDto;
import com.summitsync.api.trainerapplication.AcceptStatus;
import lombok.Data;

import java.util.List;

@Data
public class TrainerApplicationDto {
    long id;
    AcceptStatus accepted;
    String firstName;
    String lastName;
    String email;
    String phone;
    List<QualificationDto> qualificationDTOs;
    String subjectId;
}
