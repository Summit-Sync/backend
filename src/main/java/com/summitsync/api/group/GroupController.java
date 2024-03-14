package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper mapper;

    @PostMapping
    public ResponseEntity<GroupGetDTO> createGroupFromTemplate(@RequestBody CreateGroupWrapperDTO wrapper) {
        Group group = this.service.createFromTemplate(wrapper.getTemplateId(), this.mapper.mapGroupPostDTOToGroup(wrapper.getGroup()));
        return new ResponseEntity<>(this.mapper.mapGroupToGroupGetDto(group), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupGetDTO> deleteGroup(@PathVariable long id) {
        Group group = this.service.deleteById(id);
        GroupGetDTO dto = this.mapper.mapGroupToGroupGetDto(group);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupGetDTO> getGroupById(@PathVariable long id) {
        Group group = this.service.get(id);
        GroupGetDTO response = this.mapper.mapGroupToGroupGetDto(group);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GroupGetDTO>> getAllGroups() {
        List<Group> all = this.service.getAll();
        List<GroupGetDTO> DTOs = new ArrayList<>();
        for (Group group : all) {
            DTOs.add(this.mapper.mapGroupToGroupGetDto(group));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupGetDTO> updateGroup(@RequestBody GroupPostDTO dto, @PathVariable long id) {
        Group groupToUpdate = this.mapper.mapGroupPostDTOToGroup(dto);
        groupToUpdate.setGroupId(id);
        Group dbGroup = this.service.update(groupToUpdate);
        GroupGetDTO response = this.mapper.mapGroupToGroupGetDto(dbGroup);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
