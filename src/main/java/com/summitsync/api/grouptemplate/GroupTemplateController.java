package com.summitsync.api.grouptemplate;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/template/group")
@RequiredArgsConstructor
public class GroupTemplateController {

    private final GroupTemplateService groupTemplateService;
    private final GroupTemplateMapper groupTemplateMapper;
    private final QualificationService qualificationService;
    @PostMapping
    public GroupTemplateGetDTO createGroupTemplate(@RequestBody GroupTemplatePostDTO dto) {
        GroupTemplate templateToCreate = this.groupTemplateMapper.mapGroupPostDtoToGroupTemplate(dto);
        GroupTemplate createdTemplate = this.groupTemplateService.createTemplate(templateToCreate);

        return this.groupTemplateMapper.mapGroupTemplateToGroupTemplateGetDTO(createdTemplate);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable long id) {
        var groupTemplate = this.groupTemplateService.findById(id);
        this.groupTemplateService.deleteTemplate(groupTemplate);
    }

    @PutMapping("/{id}")
    public GroupTemplateGetDTO updateTemplate(@RequestBody GroupTemplatePostDTO dto, @PathVariable long id) {
        GroupTemplate templateToUpdate = this.groupTemplateMapper.mapGroupPostDtoToGroupTemplate(dto);
        templateToUpdate.setBaseTemplateId(id);
        GroupTemplate dbTemplate = this.groupTemplateService.updateTemplate(templateToUpdate);
        return this.groupTemplateMapper.mapGroupTemplateToGroupTemplateGetDTO(dbTemplate);
    }

    @GetMapping("/{id}")
    public GroupTemplateGetDTO getTemplateById(@PathVariable long id) {
        GroupTemplate template = this.groupTemplateService.findById(id);
        return this.groupTemplateMapper.mapGroupTemplateToGroupTemplateGetDTO(template);
    }

    @GetMapping
    public Set<GroupTemplateGetDTO> getAllTemplates() {
        List<GroupTemplate> all = this.groupTemplateService.getAll();
        return all
                .stream()
                .map(this.groupTemplateMapper::mapGroupTemplateToGroupTemplateGetDTO)
                .collect(Collectors.toSet());
    }

    @PutMapping("/{id}/qualfication/{qualificationId}")
    public GroupTemplateGetDTO addQualificationToGetTemplate(@PathVariable long id, @PathVariable long qualificationId) {
        var groupTemplate = this.groupTemplateService.findById(id);
        var qualification = this.qualificationService.findById(qualificationId);

        var updatedGroupTemplate = this.groupTemplateService.addQualificationToGroupTemplate(groupTemplate, qualification);

        return this.groupTemplateMapper.mapGroupTemplateToGroupTemplateGetDTO(updatedGroupTemplate);
    }
}
