package com.summitsync.api.qualification;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.dto.QualificationDto;
import com.summitsync.api.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QualificationService {

    private final QualificationRepository repository;
    private final QualificationMapper mapper;
    private final Logger log= LoggerFactory.getLogger(QualificationService.class);

    public Qualification findById(long id){
        Optional<Qualification>data=repository.findById(id);
        if(data.isEmpty()){
            log.info("Qualification on id "+id+" does not exist");
            throw new ResourceNotFoundException("Qualification on id "+id+" does not exist");
        }
        return data.get();
    }

    public Qualification saveQualification(Qualification qualification) {
        return this.repository.save(qualification);
    }

    public List<Qualification> getAllQualifications() {
        return this.repository.findAll();
    }

    public void deleteQualificationById(long id) {
        var qualification = this.findById(id);
        this.repository.delete(qualification);
    }

    public Qualification updateQualification(long id, Qualification qualification) {
        var savedQualification = this.findById(id);
        savedQualification.setName(qualification.getName());

        return this.saveQualification(savedQualification);
    }

    public QualificationDto addTrainerList(Qualification qualification, List<Long> ids) {
        qualification.getTrainers().forEach(trainer -> trainer.getQualifications().add(qualification));
        return this.mapper.mapQualificationToQualificationDto(this.repository.save(qualification));
    }
}
