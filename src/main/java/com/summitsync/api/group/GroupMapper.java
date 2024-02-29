package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMapper {
    private final GroupTemplateMapper templateMapper;
    @Autowired
    public GroupMapper() {
        templateMapper = new GroupTemplateMapper();
    }
    public GroupDTO mapGroupToGroupDto(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setDescription(group.getDescription());
        dto.setNotes(group.getNotes());
        dto.setTemplate(templateMapper.mapGroupTemplateToGroupDto(group.getTemplate()));
        dto.setNumberOfParticipants(group.getNumberOfParticipants());
        dto.setPricePerParticipant(group.getPricePerParticipant());
        dto.setRequiredQualifications(group.getRequiredQualifications());
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
        return group;
    }
}
