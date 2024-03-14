package com.summitsync.api.trainer;

import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.dto.AddTrainerDto;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainer")
public class TrainerController {

    private final TrainerService trainerService;
    private final QualificationService qualificationService;
    @PostMapping
    public TrainerDto newTrainer(@RequestBody AddTrainerDto addTrainerDto, JwtAuthenticationToken jwt) {
        return this.trainerService.newTrainer(addTrainerDto, jwt.getToken().getTokenValue());
    }

    @PostMapping("/{trainerId}/qualification/{qualificationId}")
    public TrainerDto addQualification(@PathVariable long trainerId, @PathVariable long qualificationId, JwtAuthenticationToken jwt) {
        var trainer = this.trainerService.findById(trainerId);
        var qualification = this.qualificationService.findById(qualificationId);
        return this.trainerService.addQualificationToTrainer(trainer, qualification, jwt.getToken().getTokenValue());
    }

    @DeleteMapping("/{trainerId}/qualification/{qualificationId}")
    public TrainerDto deleteQualification(@PathVariable long trainerId, @PathVariable long qualificationId, JwtAuthenticationToken jwt) {
        var trainer = this.trainerService.findById(trainerId);
        var qualification = this.qualificationService.findById(qualificationId);
        return this.trainerService.deleteTrainerQualification(trainer, qualification, jwt.getToken().getTokenValue());
    }

    @GetMapping("/{trainerId}")
    public TrainerDto getTrainerById(@PathVariable long trainerId, JwtAuthenticationToken jwt) {
        return this.trainerService.getTrainer(trainerId, jwt.getToken().getTokenValue());
    }

    @DeleteMapping("/{trainerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainerById(@PathVariable long trainerId, JwtAuthenticationToken jwt) {
        this.trainerService.deleteTrainerById(trainerId, jwt.getToken().getTokenValue());
    }

    @PutMapping("/{trainerId}")
    public TrainerDto updateTrainer(@PathVariable long trainerId, @RequestBody AddTrainerDto addTrainerDto, JwtAuthenticationToken jwt) {
        var trainer = this.trainerService.findById(trainerId);

        return this.trainerService.updateTrainer(trainer, addTrainerDto, jwt.getToken().getTokenValue());
    }
}
