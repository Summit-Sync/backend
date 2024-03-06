package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplate;
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

    public Group create(Group group) { return repository.save(group); }

    public Group createFromTemplate(GroupTemplate template, Group group) {
        group.setTemplate(template);
        return create(group);
    }
    public Group update(Group group) {
        Optional<Group> data = findById(group.getGroupId());
        if (data.isEmpty()) {
            log.info("Group with id {} does not exist", group.getGroupId());
            throw new RuntimeException("Group with id " + group.getGroupId() + " does not exist");
        }
        Group dbGroup = data.get();
        dbGroup.setContact(group.getContact());
        dbGroup.setNotes(group.getNotes());
        dbGroup.setDescription(group.getDescription());
        dbGroup.setLocation(group.getLocation());
        dbGroup.setPeriod(group.getPeriod());
        dbGroup.setNumberOfDates(group.getNumberOfDates());
        dbGroup.setNumberOfParticipants(group.getNumberOfParticipants());
        dbGroup.setPricePerParticipant(group.getPricePerParticipant());
        dbGroup.setRequiredQualifications(group.getRequiredQualifications());
        dbGroup.setTemplate(group.getTemplate());
        dbGroup.setTotalPrice(group.getTotalPrice());
        return repository.save(dbGroup);
    }

    private void delete(Group group) {
        repository.delete(group);
    }

    public Group deleteById(long id) {
        Optional<Group> data = findById(id);
        if (data.isEmpty()) {
            log.info("Group with id {} does not exist", id);
            throw new RuntimeException("Group with id" + id + " does not exist");
        }
        Group dbGroup = data.get();
        delete(dbGroup);
        return dbGroup;
    }

    private Optional<Group> findById(long id) {
        return repository.findById(id);
    }

    public Group get(long id) {
        Optional<Group> data = findById(id);
        if (data.isEmpty()) {
            log.info("Group with id {} does not exist", id);
            throw new RuntimeException("Group with id " + id + " does not exist");
        }
        return data.get();
    }

    public List<Group> getAll() {
        List<Group> all = repository.findAll();
        if (all.isEmpty()) {
            log.info("GroupList is empty");
        }
        return all;
    }
}
