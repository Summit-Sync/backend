package com.summitsync.api.grouptemplate;

public class GroupTemplateMapper {

    public GroupTemplate mapDtoToTemplate(GroupTemplateDTO dto) {
        GroupTemplate template = new GroupTemplate();
        template.setTrainerKey(dto.getTrainerKey());
        template.setAcronym(dto.getAcronym());
        template.setPricePerTrainerPerHour(dto.getPricePerTrainerPerHour());
        template.setDescription(dto.getDescription());
        template.setRequiredQualifications(dto.getRequiredQualification());
        template.setBaseTemplateId(dto.getId());
        return template;
    }

    public GroupTemplateDTO mapTemplateToDto(GroupTemplate template) {
        GroupTemplateDTO dto = new GroupTemplateDTO();
        dto.setTrainerKey(template.getTrainerKey());
        dto.setAcronym(template.getAcronym());
        dto.setPricePerTrainerPerHour(template.getPricePerTrainerPerHour());
        dto.setDescription(template.getDescription());
        dto.setRequiredQualification(template.getRequiredQualifications());
        dto.setId(template.getBaseTemplateId());
        return dto;
    }

}
