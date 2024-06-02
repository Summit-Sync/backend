package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplateService;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.trainer.Trainer;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final MailService mailService;
    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    public Group create(Group group) {
        group.setGroupNumber(generateGroupNumber(group.getAcronym()));
        return this.repository.save(group);
    }

    private String generateGroupNumber(String acronym) {
        var groups = this.repository.findByAcronymOrderByGroupNumberDesc(acronym);
        int ret = groups.isEmpty() ? 1 : Integer.parseInt(groups.getFirst().getGroupNumber()) + 1;
        return String.format("%03d", ret);
    }

    public Group update(Group groupToUpdate, Group group) {
        groupToUpdate.setCancelled(group.isCancelled());
        groupToUpdate.setAcronym(group.getAcronym());
        groupToUpdate.setTitle(group.getTitle());
        groupToUpdate.setDescription(group.getDescription());
        groupToUpdate.setNumberOfDates(group.getNumberOfDates());
        groupToUpdate.setDuration(group.getDuration());
        groupToUpdate.setContact(group.getContact());
        //TODO only update removed or new dates
        var dates = new ArrayList<>(group.getDates());
        groupToUpdate.setDates(dates);
        groupToUpdate.setNumberParticipants(group.getNumberParticipants());
        groupToUpdate.setLocation(group.getLocation());
        groupToUpdate.setMeetingPoint(group.getMeetingPoint());
        groupToUpdate.setTrainerPricePerHour(group.getTrainerPricePerHour());
        groupToUpdate.setPricePerParticipant(group.getPricePerParticipant());
        var qualifications = new ArrayList<>(group.getQualifications());
        groupToUpdate.setQualifications(qualifications);
        groupToUpdate.setParticipantsPerTrainer(group.getParticipantsPerTrainer());
        var trainers = new ArrayList<>(group.getTrainers());
        groupToUpdate.setTrainers(trainers);

        return this.repository.save(groupToUpdate);
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

    public Group addTrainer(Group group, Set<Trainer> trainer) {
        var oldTrainers = group.getTrainers();
        oldTrainers.addAll(trainer);
        group.setTrainers(oldTrainers);

        return this.repository.save(group);
    }

    public Group removeTrainer(Group group, Long trainerId) {
        var trainers = group.getTrainers();

        var updatedTrainers = trainers
                .stream()
                .filter(
                        t -> t.getTrainerId() != trainerId
                )
                .toList();

        group.setTrainers(updatedTrainers);
        return this.repository.save(group);
    }

    public Group cancel(Group group, String jwt) {
        group.setCancelled(true);
        Group canceledGroup = this.repository.save(group);
        mailService.sendGroupCancelMail(canceledGroup, jwt);
        return canceledGroup;
    }

}
