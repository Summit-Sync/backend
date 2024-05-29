package com.summitsync.api.coursetemplate;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.Qualification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseTemplateService {

    private final Logger log = LoggerFactory.getLogger(CourseTemplateService.class);
    private final CourseTemplateRepository repository;

    public CourseTemplate createCourse(CourseTemplate newTemplate) {
        return this.repository.save(newTemplate);
    }

    public CourseTemplate updateCourse(CourseTemplate courseTemplateToUpdate, CourseTemplate updatedCourseTemplate){
        courseTemplateToUpdate.setAcronym(updatedCourseTemplate.getAcronym());
        courseTemplateToUpdate.setTitle(updatedCourseTemplate.getTitle());
        courseTemplateToUpdate.setDescription(updatedCourseTemplate.getDescription());
        courseTemplateToUpdate.setNumberOfDates(updatedCourseTemplate.getNumberOfDates());
        courseTemplateToUpdate.setDuration(updatedCourseTemplate.getDuration());
        courseTemplateToUpdate.setNumberParticipants(updatedCourseTemplate.getNumberParticipants());
        courseTemplateToUpdate.setNumberWaitlist(updatedCourseTemplate.getNumberWaitlist());
        courseTemplateToUpdate.setLocation(updatedCourseTemplate.getLocation());
        courseTemplateToUpdate.setMeetingPoint(updatedCourseTemplate.getMeetingPoint());

        var prices = new ArrayList<>(updatedCourseTemplate.getPrices());
        courseTemplateToUpdate.setPrices(prices);

        var qualifications = new ArrayList<>(updatedCourseTemplate.getQualifications());
        courseTemplateToUpdate.setQualifications(qualifications);

        courseTemplateToUpdate.setNumberTrainer(updatedCourseTemplate.getNumberTrainer());

        return this.repository.save(courseTemplateToUpdate);
    }
    public CourseTemplate findById(long id){
        var courseTemplate = this.repository.findById(id);

        if (courseTemplate.isEmpty()) {
            throw new ResourceNotFoundException("CourseTemplate on id " + id + "not found");
        }

        return courseTemplate.get();
    }

    public void deleteById(Long id){
        if(this.repository.findById(id).isEmpty()){
            log.info("CourseTemplate with id {} does not exist",id);
            throw new RuntimeException("CourseTemplate with id "+id+" does not exist");
        }
        this.repository.deleteById(id);
    }

    public List<CourseTemplate> findAll(){
        return this.repository.findAll();
    }

}
