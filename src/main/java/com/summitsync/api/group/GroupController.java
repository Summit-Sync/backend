package com.summitsync.api.group;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository repository;
    private final GroupMapper mapper;

    @PostMapping
    private ResponseEntity<GroupDTO> createGroupFromTemplate(@RequestBody CreateWrapperDTO wrapper) {
        var dto = wrapper.getGroup();
        var template = wrapper.getTemplate();
        dto.setTemplate(template);
        Group group = mapper.mapGroupDTOToGroup(dto);
        repository.save(group);
        return new ResponseEntity<>(mapper.mapGroupToGroupDto(group), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<GroupDTO> deleteGroup(@PathVariable long id) {
        Optional<Group> group = repository.findById(id);
        if (group.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapGroupToGroupDto(group.get()), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GroupDTO> getGroupById(@PathVariable long id) {
        Optional<Group> group = repository.findById(id);
        if (group.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.mapper.mapGroupToGroupDto(group.get()), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<GroupDTO>> getAllGroups() {
        List<Group> all = this.repository.findAll();
        List<GroupDTO> dtos = new ArrayList<>();
        for (Group group : all) {
            dtos.add(this.mapper.mapGroupToGroupDto(group));
        }
        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GroupDTO> updateGroup(@RequestBody GroupDTO dto, @PathVariable long id) {
        Optional<Group> group = repository.findById(id);
        if (group.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Group newGroup = mapper.mapGroupDTOToGroup(dto);
        this.repository.save(newGroup);
        return new ResponseEntity<>(this.mapper.mapGroupToGroupDto(newGroup), HttpStatus.OK);
    }
}
