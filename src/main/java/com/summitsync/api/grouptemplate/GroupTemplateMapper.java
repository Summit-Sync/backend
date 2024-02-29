package com.summitsync.api.grouptemplate;

import org.springframework.stereotype.Service;

@Service
public class GroupTemplateMapper {

    public GroupTemplate mapGroupDtoToGroupTemplate(GroupTemplateDTO dto) {
        GroupTemplate template = new GroupTemplate();
        template.setTrainerKey(dto.getTrainerKey());
        template.setAcronym(dto.getAcronym());
        template.setPricePerTrainerPerHour(dto.getPricePerTrainerPerHour());
        template.setDescription(dto.getDescription());
        template.setRequiredQualifications(dto.getRequiredQualification());
        return template;
    }

    public GroupTemplateDTO mapGroupTemplateToGroupDto(GroupTemplate template) {
        GroupTemplateDTO dto = new GroupTemplateDTO();
        dto.setTrainerKey(template.getTrainerKey());
        dto.setAcronym(template.getAcronym());
        dto.setPricePerTrainerPerHour(template.getPricePerTrainerPerHour());
        dto.setDescription(template.getDescription());
        dto.setRequiredQualification(template.getRequiredQualifications());
        return dto;
    }

}
