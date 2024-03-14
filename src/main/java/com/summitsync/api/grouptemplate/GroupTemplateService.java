package com.summitsync.api.grouptemplate;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.qualification.Qualification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupTemplateService {

    private final GroupTemplateRepository repository;
    private final Logger log = LoggerFactory.getLogger(GroupTemplateService.class);

    public GroupTemplate createTemplate(GroupTemplate template) {
        return this.repository.save(template);
    }

    public GroupTemplate updateTemplate(GroupTemplate template) {
        var dbTemplate = this.findById(template.getBaseTemplateId());
        dbTemplate.setAcronym(template.getAcronym());
        dbTemplate.setDescription(template.getDescription());
        dbTemplate.setTitle(template.getTitle());
        dbTemplate.setNumberOfDates(template.getNumberOfDates());
        dbTemplate.setDurationInMinutes(template.getDurationInMinutes());
        dbTemplate.setDuration(template.getDuration());
        dbTemplate.setPricePerTrainerPerHour(template.getPricePerTrainerPerHour());
        dbTemplate.setRequiredQualifications(template.getRequiredQualifications());
        dbTemplate.setTrainerKey(template.getTrainerKey());
        return this.repository.save(dbTemplate);
    }

    public void deleteTemplate(GroupTemplate template) {
        this.repository.delete(template);
    }


    public GroupTemplate findById(long id) {
        var groupTemplate =  this.repository.findById(id);
        if (groupTemplate.isEmpty()) {
            throw new ResourceNotFoundException("GroupTemplate on id " + id + " not found");
        }

        return groupTemplate.get();
    }

    public List<GroupTemplate> getAll() {
        return this.repository.findAll();
    }

    public GroupTemplate addQualificationToGroupTemplate(GroupTemplate groupTemplate, Qualification qualification) {
        var qualifications = groupTemplate.getRequiredQualifications();
        qualifications.add(qualification);

        groupTemplate.setRequiredQualifications(qualifications);

        return this.repository.save(groupTemplate);
    }
}
