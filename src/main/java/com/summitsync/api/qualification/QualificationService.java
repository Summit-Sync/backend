package com.summitsync.api.qualification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Qualification on id "+id+" does not exist");
        }
        return data.get();
    }

}
