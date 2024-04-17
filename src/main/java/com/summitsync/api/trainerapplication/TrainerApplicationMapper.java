package com.summitsync.api.trainerapplication;

import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainerapplication.dto.TrainerApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerApplicationMapper {
    private final TrainerMapper trainerMapper;
    public TrainerApplicationDto mapTrainerApplicationToTrainerApplicationDto(TrainerApplication application, JwtAuthenticationToken jwt) {
        TrainerApplicationDto dto = new TrainerApplicationDto();
        dto.setId(application.getApplicationId());
        var trainerDto = trainerMapper.mapTrainerToTrainerDto(application.getTrainer(), jwt.getToken().getTokenValue());
        dto.setPhone(trainerDto.getPhone());
        dto.setSubjectId(trainerDto.getSubjectId());
        dto.setQualificationDTOs(trainerDto.getQualifications());
        dto.setLastName(trainerDto.getLastName());
        dto.setFirstName(trainerDto.getFirstName());
        dto.setAccepted(application.getAccepted());
        dto.setEmail(trainerDto.getEmail());
        return dto;
    }
}
