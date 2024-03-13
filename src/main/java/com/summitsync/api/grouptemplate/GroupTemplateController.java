package com.summitsync.api.grouptemplate;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/grouptemplate")
@RequiredArgsConstructor
public class GroupTemplateController {

    private final GroupTemplateService service;
    private final GroupTemplateMapper mapper;
    @PostMapping
    public ResponseEntity<GroupTemplateGetDTO> createTemplate(@RequestBody GroupTemplatePostDTO dto) {
        GroupTemplate templateToCreate = this.mapper.mapGroupPostDtoToGroupTemplate(dto);
        GroupTemplate createdTemplate = this.service.createTemplate(templateToCreate);
        GroupTemplateGetDTO response = this.mapper.mapGroupTemplateToGroupTemplateGetDTO(createdTemplate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable long id) {
        this.service.deleteTemplateById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupTemplateGetDTO> updateTemplate(@RequestBody GroupTemplatePostDTO dto, @PathVariable long id) {
        GroupTemplate templateToUpdate = this.mapper.mapGroupPostDtoToGroupTemplate(dto);
        templateToUpdate.setBaseTemplateId(id);
        GroupTemplate dbTemplate = this.service.updateTemplate(templateToUpdate);
        GroupTemplateGetDTO response = this.mapper.mapGroupTemplateToGroupTemplateGetDTO(dbTemplate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupTemplateGetDTO> getTemplateById(@PathVariable long id) {
        GroupTemplate template = this.service.get(id);
        GroupTemplateGetDTO response = this.mapper.mapGroupTemplateToGroupTemplateGetDTO(template);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GroupTemplateGetDTO>> getAllTemplates() {
        List<GroupTemplate> all = this.service.getAll();
        List<GroupTemplateGetDTO> DTOs = new ArrayList<>();
        for (GroupTemplate template : all) {
            DTOs.add(this.mapper.mapGroupTemplateToGroupTemplateGetDTO(template));
        }
        return new ResponseEntity<>(DTOs, HttpStatus.OK);
    }
}
