package com.summitsync.api.grouptemplate;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.location.LocationRepository;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupTemplateService {

    private final GroupTemplateRepository repository;
    private final LocationService locationService;
    private final QualificationService qualificationService;
    private final Logger log = LoggerFactory.getLogger(GroupTemplateService.class);

    public GroupTemplate createTemplate(GroupTemplate template) {
        return this.repository.save(template);
    }

    public GroupTemplate updateTemplate(GroupTemplate template, GroupTemplatePostDTO updated) {
        template.setAcronym(updated.getAcronym());
        template.setTitle(updated.getTitle());
        template.setDescription(updated.getDescription());
        template.setNumberOfDates(updated.getNumberOfDates());
        template.setDuration(updated.getDuration());
        template.setLocation(this.locationService.getLocationById(updated.getLocation()));
        template.setMeetingPoint(updated.getMeetingPoint());
        template.setTrainerPricerPerHour(updated.getTrainerPricePerHour());
        template.setPricePerParticipant(updated.getPricePerParticipant());
        template.setRequiredQualifications(updated.getRequiredQualificationList().stream().map(this.qualificationService::findById).collect(Collectors.toSet()));
        template.setParticipantsPerTrainer(updated.getParticipantsPerTrainer());
        return this.repository.save(template);
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
}
