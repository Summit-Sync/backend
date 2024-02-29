package com.summitsync.api.coursetemplate;

import com.summitsync.api.qualification.Qualification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CourseTemplateService {

    private final Logger log= LoggerFactory.getLogger(CourseTemplateService.class);
    private final CourseTemplateRepository repository;

    public CourseTemplate createCourse(CourseTemplate newTemplate){
        return repository.save(newTemplate);
    }
    public CourseTemplate updateCourse(CourseTemplate updatedTemplate, Long id){
        Optional<CourseTemplate>data=findById(id);
        if(data.isEmpty()){
            log.info("CourseTemplate with id {} does not exist",id);
            throw new RuntimeException("CourseTemplate with id "+id+" does not exist");
        }
        CourseTemplate template=data.get();
        template.setNumberOfParticipants(updatedTemplate.getNumberOfParticipants());
        template.setPriceList(updatedTemplate.getPriceList());
        template.setNumberOfTrainers(updatedTemplate.getNumberOfTrainers());
        template.setNumberOfWaitList(updatedTemplate.getNumberOfWaitList());
        template.setAcronym(updatedTemplate.getAcronym());
        template.setDescription(updatedTemplate.getDescription());
        template.setNumberOfDates(updatedTemplate.getNumberOfDates());
        template.setTitle(updatedTemplate.getTitle());
        template.setDuration(updatedTemplate.getDuration());
        List<Qualification>qualificationList=new ArrayList<>();
        //TODO add qualifications to List
        template.setRequiredQualifications(qualificationList);
        return repository.save(template);
    }
    public Optional<CourseTemplate>findById(long id){
        return repository.findById(id);
    }

    public void deleteById(Long id){
        if(repository.findById(id).isEmpty()){
            log.info("CourseTemplate with id {} does not exist",id);
            throw new RuntimeException("CourseTemplate with id "+id+" does not exist");
        }
        repository.deleteById(id);
    }

    public List<CourseTemplate> findAll(){
        return repository.findAll();
    }
}
