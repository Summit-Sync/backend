package com.summitsync.api.group;

import com.summitsync.api.date.EventDate;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final Logger log = LoggerFactory.getLogger(GroupService.class);
    private final GroupTemplateService templateService;

    public Group create(Group group) { return this.repository.save(group); }

    public Group update(Group groupToUpdate, Group group) {
        groupToUpdate.setCancelled(group.isCancelled());
        groupToUpdate.setTitle(group.getTitle());
        groupToUpdate.setDescription(group.getDescription());
        groupToUpdate.setNumberOfDates(group.getNumberOfDates());
        groupToUpdate.setDuration(group.getDuration());
        groupToUpdate.setContact(group.getContact());
        groupToUpdate.setDates(group.getDates());
        groupToUpdate.setNumberParticipants(group.getNumberParticipants());
        groupToUpdate.setLocation(group.getLocation());
        groupToUpdate.setMeetingPoint(group.getMeetingPoint());
        groupToUpdate.setTrainerPricePerHour(group.getTrainerPricePerHour());
        groupToUpdate.setPricePerParticipant(group.getPricePerParticipant());
        groupToUpdate.setQualifications(group.getQualifications());
        groupToUpdate.setParticipantsPerTrainer(group.getParticipantsPerTrainer());
        groupToUpdate.setTrainers(group.getTrainers());

        return groupToUpdate;
    }

    private void delete(Group group) {
        this.repository.delete(group);
    }

    public Group deleteById(long id) {
        Optional<Group> data = this.findById(id);
        if (data.isEmpty()) {
            log.info("Group with id {} does not exist", id);
            throw new RuntimeException("Group with id" + id + " does not exist");
        }
        Group dbGroup = data.get();
        delete(dbGroup);
        return dbGroup;
    }

    private Optional<Group> findById(long id) {
        return this.repository.findById(id);
    }

    public Group get(long id) {
        Optional<Group> data = this.findById(id);
        if (data.isEmpty()) {
            log.info("Group with id {} does not exist", id);
            throw new RuntimeException("Group with id " + id + " does not exist");
        }
        return data.get();
    }

    public List<Group> getAll() {
        List<Group> all = this.repository.findAll();
        if (all.isEmpty()) {
            log.info("GroupList is empty");
        }
        return all;
    }
}
