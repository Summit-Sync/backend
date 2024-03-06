package com.summitsync.api.grouptemplate;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import org.springframework.stereotype.Service;

@Service
public class GroupTemplateMapper {

    public GroupTemplate mapGroupPostDtoToGroupTemplate(GroupTemplatePostDTO dto) {
        GroupTemplate template = new GroupTemplate();
        template.setTrainerKey(dto.getTrainerKey());
        template.setAcronym(dto.getAcronym());
        template.setPricePerTrainerPerHour(dto.getPricePerTrainerPerHour());
        template.setDescription(dto.getDescription());
        template.setRequiredQualifications(dto.getRequiredQualification());
        return template;
    }

    public GroupTemplateGetDTO mapGroupTemplateToGroupTemplateGetDTO(GroupTemplate template) {
        GroupTemplateGetDTO dto = new GroupTemplateGetDTO();
        dto.setId(template.getBaseTemplateId());
        dto.setTrainerKey(template.getTrainerKey());
        dto.setAcronym(template.getAcronym());
        dto.setPricePerTrainerPerHour(template.getPricePerTrainerPerHour());
        dto.setDescription(template.getDescription());
        dto.setRequiredQualification(template.getRequiredQualifications());
        return dto;
    }

}
