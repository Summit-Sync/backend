package com.summitsync.api.grouptemplate;

import com.summitsync.api.group.Group;
import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/grouptemplate")
@RequiredArgsConstructor
public class GroupTemplateController {

    private final GroupTemplateService service;
    private final GroupTemplateMapper mapper;
    @PostMapping
    private ResponseEntity<GroupTemplateGetDTO> createTemplate(@RequestBody GroupTemplatePostDTO dto) {
        GroupTemplate templateToCreate = mapper.mapGroupPostDtoToGroupTemplate(dto);
        GroupTemplate createdTemplate = service.createTemplate(templateToCreate);
        GroupTemplateGetDTO response = mapper.mapGroupTemplateToGroupTemplateGetDTO(createdTemplate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    private ResponseEntity<GroupTemplateGetDTO> deleteTemplate(@PathVariable long id) {
        GroupTemplate template = service.deleteTemplateById(id);
        GroupTemplateGetDTO dto = mapper.mapGroupTemplateToGroupTemplateGetDTO(template);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GroupTemplateGetDTO> updateTemplate(@RequestBody GroupTemplatePostDTO dto, @PathVariable long id) {
        GroupTemplate templateToUpdate = mapper.mapGroupPostDtoToGroupTemplate(dto);
        templateToUpdate.setBaseTemplateId(id);
        GroupTemplate dbTemplate = service.updateTemplate(templateToUpdate);
        GroupTemplateGetDTO response = mapper.mapGroupTemplateToGroupTemplateGetDTO(dbTemplate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GroupTemplateGetDTO> getTemplateById(@PathVariable long id) {
        GroupTemplate template = service.get(id);
        GroupTemplateGetDTO response = mapper.mapGroupTemplateToGroupTemplateGetDTO(template);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<GroupTemplateGetDTO>> getAllTemplates() {
        List<GroupTemplate> all = this.service.getAll();
        List<GroupTemplateGetDTO> DTOs = new ArrayList<>();
        for (GroupTemplate template : all) {
            DTOs.add(this.mapper.mapGroupTemplateToGroupTemplateGetDTO(template));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }
}
