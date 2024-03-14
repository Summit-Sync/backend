package com.summitsync.api.grouptemplate;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.qualification.QualificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupTemplateMapper {
    private final QualificationMapper qualificationMapper;
    public GroupTemplate mapGroupPostDtoToGroupTemplate(GroupTemplatePostDTO dto) {
        GroupTemplate template = new GroupTemplate();
        template.setTrainerKey(dto.getTrainerKey());
        template.setAcronym(dto.getAcronym());
        template.setPricePerTrainerPerHour(dto.getPricePerTrainerPerHour());
        template.setDescription(dto.getDescription());
        template.setTitle(dto.getTitle());
        return template;
    }

    public GroupTemplateGetDTO mapGroupTemplateToGroupTemplateGetDTO(GroupTemplate template) {
        GroupTemplateGetDTO dto = new GroupTemplateGetDTO();
        dto.setId(template.getBaseTemplateId());
        dto.setTrainerKey(template.getTrainerKey());
        dto.setAcronym(template.getAcronym());
        dto.setPricePerTrainerPerHour(template.getPricePerTrainerPerHour());
        dto.setDescription(template.getDescription());
        dto.setRequiredQualification(
                template.getRequiredQualifications()
                        .stream()
                        .map(this.qualificationMapper::mapQualificationToQualificationDto)
                        .collect(Collectors.toSet())
        );

        dto.setTitle(template.getTitle());
        return dto;
    }
}
