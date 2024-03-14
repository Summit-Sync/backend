package com.summitsync.api.qualification;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.dto.AddQualificationDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QualificationService {

    private final QualificationRepository repository;

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
}
