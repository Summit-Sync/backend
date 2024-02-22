package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;

public class GroupMapper {
    private GroupTemplateMapper templateMapper;
    public GroupDTO mapGroupToGroupDto(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setDescription(group.getDescription());
        dto.setNotes(group.getNotes());
        dto.setTemplate(templateMapper.mapGroupTemplateToGroupDto(group.getTemplate()));
        dto.setNumberOfParticipants(group.getNumberOfParticipants());
        dto.setPricePerParticipant(group.getPricePerParticipant());
        dto.setRequiredQualifications(group.getRequiredQualifications());
        dto.setId(group.getGroupId());
        return dto;
    }
    public Group mapGroupDTOToGroup(GroupDTO dto) {
        Group group = new Group();
        GroupTemplate template = templateMapper.mapGroupDtoToGroupTemplate(dto.getTemplate());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setNotes(dto.getNotes());
        group.setNumberOfParticipants(dto.getNumberOfParticipants());
        group.setRequiredQualifications(dto.getRequiredQualifications() == null ? template.getRequiredQualifications() : dto.getRequiredQualifications());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setPricePerParticipant(dto.getPricePerParticipant());
        group.setGroupId(dto.getId());
        return group;
    }
}
