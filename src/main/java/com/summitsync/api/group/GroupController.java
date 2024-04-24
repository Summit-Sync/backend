package com.summitsync.api.group;

import com.summitsync.api.date.EventDateService;
import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper mapper;
    private final EventDateService eventDateService;

    @PostMapping
    public ResponseEntity<GroupGetDTO> createGroup(@RequestBody @Valid GroupPostDTO dto, JwtAuthenticationToken jwt) {
        Group group = this.mapper.mapGroupPostDTOToGroup(dto);
        var createdGroup = this.service.create(group);
        return new ResponseEntity<>(this.mapper.mapGroupToGroupGetDto(createdGroup, jwt.getToken().getTokenValue()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupGetDTO> deleteGroup(@PathVariable long id, JwtAuthenticationToken jwt) {
        Group group = this.service.deleteById(id);
        GroupGetDTO dto = this.mapper.mapGroupToGroupGetDto(group, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupGetDTO> getGroupById(@PathVariable long id, JwtAuthenticationToken jwt) {
        Group group = this.service.get(id);
        GroupGetDTO response = this.mapper.mapGroupToGroupGetDto(group, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GroupGetDTO>> getAllGroups(JwtAuthenticationToken jwt) {
        List<Group> all = this.service.getAll();
        List<GroupGetDTO> DTOs = new ArrayList<>();
        for (Group group : all) {
            DTOs.add(this.mapper.mapGroupToGroupGetDto(group, jwt.getToken().getTokenValue()));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupGetDTO> updateGroup(@RequestBody @Valid GroupPostDTO dto, @PathVariable long id, JwtAuthenticationToken jwt) {
        var group = this.mapper.mapGroupPostDTOToGroup(dto);
        var groupToUpdate = this.service.get(id);
        Group dbGroup = this.service.update(groupToUpdate, group);
        GroupGetDTO response = this.mapper.mapGroupToGroupGetDto(dbGroup, jwt.getToken().getTokenValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<GroupGetDTO> cancelGroup(@PathVariable long id, JwtAuthenticationToken jwt) {
        var group = this.service.get(id);
        var updatedGroup = this.service.cancel(group);

        return ResponseEntity.ok(this.mapper.mapGroupToGroupGetDto(updatedGroup, jwt.getToken().getTokenValue()));
    }
}
