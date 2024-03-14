package com.summitsync.api.grouptemplate;

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
        Optional<GroupTemplate> data = this.findById(template.getBaseTemplateId());
        if (data.isEmpty()) {
            log.info("GroupTemplate with id {} does not exist", template.getBaseTemplateId());
            throw new RuntimeException("GroupTemplate with id " + template.getBaseTemplateId() + " does not exist");
        }
        GroupTemplate dbTemplate = data.get();
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

    private void deleteTemplate(GroupTemplate template) {
        this.repository.delete(template);
    }

    public GroupTemplate deleteTemplateById(long id) {
        Optional<GroupTemplate> data = this.findById(id);
        if (data.isEmpty()) {
            log.info("GroupTemplate with id {} does not exist", id);
            throw new RuntimeException("GroupTemplate with id" + id + " does not exist");
        }
        GroupTemplate dbTemplate = data.get();
        deleteTemplate(dbTemplate);
        return dbTemplate;
    }

    private Optional<GroupTemplate> findById(long id) {
        return this.repository.findById(id);
    }

    public GroupTemplate get(long id) {
        Optional<GroupTemplate> data = this.findById(id);
        if (data.isEmpty()) {
            log.info("GroupTemplate with id {} does not exist", id);
            throw new RuntimeException("GroupTemplate with id " + id + " does not exist");
        }
        return data.get();
    }

    public List<GroupTemplate> getAll() {
        List<GroupTemplate> all = this.repository.findAll();
        if (all.isEmpty()) {
            log.info("GroupTemplateList is empty");
        }
        return all;
    }
}
