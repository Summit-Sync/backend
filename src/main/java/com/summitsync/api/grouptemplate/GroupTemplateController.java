package com.summitsync.api.grouptemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController("/api/v1/grouptemplate")
public class GroupTemplateController {

    @Autowired
    private GroupTemplateRepository repository;
    private GroupTemplateMapper mapper;
    @PostMapping
    private ResponseEntity<GroupTemplateDTO> createTemplate(@RequestBody GroupTemplateDTO dto) {
        repository.save(this.mapper.mapDtoToTemplate(dto));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @DeleteMapping
    private ResponseEntity<GroupTemplateDTO> deleteTemplate(@PathVariable long id) {
        Optional<GroupTemplate> template = repository.findById(id);
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapTemplateToDto(template.get()), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<GroupTemplateDTO> getTemplateById(@PathVariable long id) {
        Optional<GroupTemplate> template = this.repository.findById(id);
        if (template.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.mapper.mapTemplateToDto(template.get()), HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<List<GroupTemplateDTO>> getAllTemplates() {
        List<GroupTemplate> all = this.repository.findAll();
        List<GroupTemplateDTO> dtos = new ArrayList<>();
        for (GroupTemplate template : all) {
            dtos.add(this.mapper.mapTemplateToDto(template));
        }
        if (all.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
