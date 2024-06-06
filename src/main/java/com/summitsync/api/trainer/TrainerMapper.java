package com.summitsync.api.trainer;

import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.keycloak.dto.KeycloakAddUserRequest;
import com.summitsync.api.keycloak.dto.KeycloakUser;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.trainer.dto.AddTrainerDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import com.summitsync.api.trainer.dto.UpdateTrainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainerMapper {
    private final QualificationMapper qualificationMapper;
    private final KeycloakRestService keycloakRestService;

    public KeycloakAddUserRequest mapAddTrainerDtoToKeycloakAddUserRequest(AddTrainerDto addTrainerDto, boolean updatePassword) {
        var groups = new ArrayList<String>();
        groups.add("trainer");

        Map<String, Object> attributesMap = new HashMap<>();
        attributesMap.put("phone", addTrainerDto.getPhone());


        var req = KeycloakAddUserRequest.builder()
                .firstName(addTrainerDto.getFirstName())
                .lastName(addTrainerDto.getLastName())
                .enabled(true)
                .username(addTrainerDto.getUsername())
                .groups(groups)
                .email(addTrainerDto.getEmail())
                .attributes(attributesMap);

        if (updatePassword) {
            req.requiredActions(Collections.singletonList("UPDATE_PASSWORD"));
        }

        return req.build();
    }

    public TrainerDto mapKeycloakUserToTrainerDto(KeycloakUser keycloakUser, Trainer trainer) {
        var qualifications = trainer.getQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).toList();

        var builder =  TrainerDto.builder()
                .id(trainer.getTrainerId())
                .subjectId(keycloakUser.getId())
                .firstName(keycloakUser.getFirstName())
                .lastName(keycloakUser.getLastName())
                .qualifications(qualifications)
                .email(keycloakUser.getEmail());

        if (keycloakUser.getAttributes() != null) {
            builder.phone(keycloakUser.getAttributes().get("phone").getFirst());
        }

        return builder.build();
    }

    public KeycloakAddUserRequest mapUpdateTrainerDtoToKeycloakAddUserRequest(UpdateTrainerDto updateTrainerDto) {
        var attributes = new HashMap<String, Object>();
        attributes.put("phone", updateTrainerDto.getPhone());
        return KeycloakAddUserRequest.builder()
                .firstName(updateTrainerDto.getFirstName())
                .lastName(updateTrainerDto.getLastName())
                .email(updateTrainerDto.getEmail())
                .attributes(attributes)
                .build();
    }

    public TrainerDto mapTrainerToTrainerDto(Trainer trainer, String jwt) {
        var keycloakUser = this.keycloakRestService.getUser(trainer.getSubjectId(), jwt);

        return this.mapKeycloakUserToTrainerDto(keycloakUser, trainer);
    }
}
