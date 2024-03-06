package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
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
    private final GroupTemplateMapper templateMapper;

    @PostMapping
    private ResponseEntity<GroupGetDTO> createGroupFromTemplate(@RequestBody CreateWrapperDTO wrapper) {
        Group group = service.createFromTemplate(templateMapper.mapGroupGetDtoToGroupTemplate(wrapper.getTemplate()), mapper.mapGroupGetDTOToGroup(wrapper.getGroup()));
        return new ResponseEntity<>(mapper.mapGroupToGroupGetDto(group), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<GroupGetDTO> deleteGroup(@PathVariable long id) {
        Group group = service.deleteById(id);
        GroupGetDTO dto = mapper.mapGroupToGroupGetDto(group);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GroupGetDTO> getGroupById(@PathVariable long id) {
        Group group = service.get(id);
        GroupGetDTO response = mapper.mapGroupToGroupGetDto(group);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<GroupGetDTO>> getAllGroups() {
        List<Group> all = this.service.getAll();
        List<GroupGetDTO> DTOs = new ArrayList<>();
        for (Group group : all) {
            DTOs.add(this.mapper.mapGroupToGroupGetDto(group));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GroupGetDTO> updateGroup(@RequestBody GroupPostDTO dto, @PathVariable long id) {
        Group groupToUpdate = mapper.mapGroupPostDTOToGroup(dto);
        groupToUpdate.setGroupId(id);
        Group dbGroup = service.update(groupToUpdate);
        GroupGetDTO response = mapper.mapGroupToGroupGetDto(dbGroup);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
