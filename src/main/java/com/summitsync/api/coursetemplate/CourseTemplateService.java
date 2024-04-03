package com.summitsync.api.coursetemplate;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetemplateprice.CourseTemplatePrice;
import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.Qualification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseTemplateService {

    private final Logger log = LoggerFactory.getLogger(CourseTemplateService.class);
    private final CourseTemplateRepository repository;

    public CourseTemplate createCourse(CourseTemplate newTemplate){
        return this.repository.save(newTemplate);
    }
    public CourseTemplate updateCourse(CourseTemplate updatedTemplate, Long id){
        var courseTemplate = this.findById(id);

        courseTemplate.setNumberOfParticipants(updatedTemplate.getNumberOfParticipants());
        courseTemplate.setNumberOfTrainers(updatedTemplate.getNumberOfTrainers());
        courseTemplate.setNumberOfWaitList(updatedTemplate.getNumberOfWaitList());
        courseTemplate.setAcronym(updatedTemplate.getAcronym());
        courseTemplate.setDescription(updatedTemplate.getDescription());
        courseTemplate.setNumberOfDates(updatedTemplate.getNumberOfDates());
        courseTemplate.setTitle(updatedTemplate.getTitle());
        courseTemplate.setDuration(updatedTemplate.getDuration());

        return this.repository.save(courseTemplate);
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

    public CourseTemplate addQualificationToCourseTemplate(CourseTemplate courseTemplate, Qualification qualification) {
        var qualifications = courseTemplate.getRequiredQualifications();
        qualifications.add(qualification);

        courseTemplate.setRequiredQualifications(qualifications);
        return this.repository.save(courseTemplate);
    }

    public CourseTemplate removeQualificationFromCourseTemplate(CourseTemplate courseTemplate, Qualification qualification) {
        var qualificationList = courseTemplate.getRequiredQualifications();

        var updatedQualificationList = qualificationList
                .stream()
                .filter(
                        q -> q.getQualificationId() != qualification.getQualificationId()
                )
                .collect(Collectors.toSet());

        courseTemplate.setRequiredQualifications(updatedQualificationList);
        return this.repository.save(courseTemplate);
    }

    public CourseTemplate addPriceToCourseTemplate(CourseTemplate courseTemplate, CourseTemplatePrice courseTemplatePrice) {
        var priceList = courseTemplate.getPriceList();
        priceList.add(courseTemplatePrice);

        courseTemplate.setPriceList(priceList);
        return this.repository.save(courseTemplate);
    }

    public CourseTemplate removePriceFromCourseTemplate(CourseTemplate courseTemplate, CourseTemplatePrice courseTemplatePrice) {
        var priceList = courseTemplate.getPriceList();

        var updatedPriceList = priceList
                .stream()
                .filter(
                        price -> price.getCourseTemplatePriceId() != courseTemplatePrice.getCourseTemplatePriceId()
                )
                .collect(Collectors.toSet());

        courseTemplate.setPriceList(updatedPriceList);
        return this.repository.save(courseTemplate);
    }
}
