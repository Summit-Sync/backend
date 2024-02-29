package com.summitsync.api.grouptemplate;

import com.summitsync.api.group.GroupMapper;
import com.summitsync.api.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/grouptemplate")
public class GroupTemplateController {

    private final GroupTemplateRepository repository;
    private final GroupTemplateMapper mapper;
    @Autowired
    public GroupTemplateController(GroupTemplateRepository repository) {
        this.repository = repository;
        this.mapper = new GroupTemplateMapper();
    }
    @PostMapping
    private ResponseEntity<GroupTemplateDTO> createTemplate(@RequestBody GroupTemplateDTO dto) {
        repository.save(this.mapper.mapGroupDtoToGroupTemplate(dto));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    private ResponseEntity<GroupTemplateDTO> deleteTemplate(@PathVariable long id) {
        Optional<GroupTemplate> template = repository.findById(id);
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapGroupTemplateToGroupDto(template.get()), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GroupTemplateDTO> updateTemplate(@RequestBody GroupTemplateDTO dto, @PathVariable long id) {
        Optional<GroupTemplate> template = repository.findById(id);
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GroupTemplate newTemplate = mapper.mapGroupDtoToGroupTemplate(dto);
        this.repository.save(newTemplate);
        return new ResponseEntity<>(this.mapper.mapGroupTemplateToGroupDto(newTemplate), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GroupTemplateDTO> getTemplateById(@PathVariable long id) {
        Optional<GroupTemplate> template = this.repository.findById(id);
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.mapper.mapGroupTemplateToGroupDto(template.get()), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<GroupTemplateDTO>> getAllTemplates() {
        List<GroupTemplate> all = this.repository.findAll();
        List<GroupTemplateDTO> dtos = new ArrayList<>();
        for (GroupTemplate template : all) {
            dtos.add(this.mapper.mapGroupTemplateToGroupDto(template));
        }
        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
