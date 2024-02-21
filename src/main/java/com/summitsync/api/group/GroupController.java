package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController("/api/v1/group")
public class GroupController {
    @Autowired
    private GroupRepository repository;
    private GroupMapper mapper;
    @PostMapping
    private ResponseEntity<GroupDTO> createGroupFromTemplate(@RequestBody GroupTemplateDTO template, GroupDTO dto) {
        dto.setTemplate(template);
        Group group = mapper.mapGroupDTOToGroup(dto);
        repository.save(group);
        return new ResponseEntity<>(mapper.mapGroupToGroupDto(group), HttpStatus.CREATED);
    }

    @DeleteMapping
    private ResponseEntity<GroupDTO> deleteGroup(@PathVariable long id) {
        Optional<Group> group = repository.findById(id);
        if (group.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapGroupToGroupDto(group.get()), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<GroupDTO> getGroup(@PathVariable long id) {
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
        if (all.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<GroupDTO> updateGroup(@RequestBody GroupDTO dto) {
        Optional<Group> group = repository.findById(dto.getId());
        if (group.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Group newGroup = mapper.mapGroupDTOToGroup(dto);
        this.repository.save(newGroup);
        return new ResponseEntity<>(this.mapper.mapGroupToGroupDto(newGroup), HttpStatus.OK);
    }
}
