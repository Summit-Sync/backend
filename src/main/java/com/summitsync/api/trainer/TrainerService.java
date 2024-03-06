package com.summitsync.api.trainer;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.keycloak.dto.KeycloakResetPasswordRequest;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.trainer.dto.AddTrainerDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerService {
    final private TrainerRepository trainerRepository;
    final private KeycloakRestService keycloakRestService;
    final private TrainerMapper trainerMapper;

    public TrainerDto getTrainer(long id, String jwt) {
        var trainer = this.findById(id);
        var keycloakTrainer = this.keycloakRestService.getUser(trainer.getSubjectId(), jwt);
        return this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakTrainer, trainer);
    }

    public Trainer findById(long id) {
        var trainer = this.trainerRepository.findById(id);

        if (trainer.isEmpty()) {
            throw new ResourceNotFoundException("trainer with id " + id + "could not be found");
        }

        return trainer.get();
    }

    public TrainerDto newTrainer(AddTrainerDto addTrainerDto, String jwt) {
        var keycloakAddUserResponse = this.keycloakRestService.addAndRetrieveUser(
                this.trainerMapper.mapAddTrainerDtoToKeycloakAddUserRequest(addTrainerDto),
                jwt
        );

        this.keycloakRestService.setUserPassword(
                KeycloakResetPasswordRequest.builder().value(addTrainerDto.getPassword()).build(),
                keycloakAddUserResponse.getId(),
                jwt
        );

        var trainer = new Trainer();
        trainer.setSubjectId(keycloakAddUserResponse.getId());
        var databaseTrainer = this.trainerRepository.save(trainer);
        return this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakAddUserResponse, databaseTrainer);
    }

    public TrainerDto addQualificationToTrainer(Trainer trainer, Qualification qualification, String jwt) {
        var qualificationList = trainer.getQualifications();
        qualificationList.add(qualification);

        var databaseTrainer = this.trainerRepository.save(trainer);

        var keycloakUser = this.keycloakRestService.getUser(trainer.getSubjectId(), jwt);

        return this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakUser, databaseTrainer);
    }

    public void deleteTrainerById(long id, String jwt) {
        var trainer = this.trainerRepository.findById(id).orElseThrow();
        this.keycloakRestService.deleteUser(trainer.getSubjectId(), jwt);
        this.trainerRepository.delete(trainer);
    }

    public TrainerDto deleteTrainerQualification(Trainer trainer, Qualification qualification, String jwt) {
        var qualificationList = trainer.getQualifications();
        qualificationList.remove(qualification);

        return this.trainerDtoFromTrainer(this.trainerRepository.save(trainer), jwt);
    }

    public TrainerDto updateTrainer(Trainer trainer, AddTrainerDto addTrainerDto, String jwt) {
        var keycloakAddUser = this.trainerMapper.mapAddTrainerDtoToKeycloakAddUserRequest(addTrainerDto);

        var keycloakUser = this.keycloakRestService.updateUser(trainer.getSubjectId(), keycloakAddUser, jwt);

        return this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakUser, trainer);
    }

    private TrainerDto trainerDtoFromTrainer(Trainer trainer, String jwt) {
        var keycloakUser = this.keycloakRestService.getUser(trainer.getSubjectId(), jwt);
        return this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakUser, trainer);
    }
}
