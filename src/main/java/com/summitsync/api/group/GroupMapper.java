package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMapper {
    private final GroupTemplateMapper templateMapper;
    public GroupGetDTO mapGroupToGroupGetDto(Group group) {
        GroupGetDTO dto = new GroupGetDTO();
        dto.setDescription(group.getDescription());
        dto.setNotes(group.getNotes());
        dto.setTemplate(templateMapper.mapGroupTemplateToGroupTemplateGetDTO(group.getTemplate()));
        dto.setNumberOfParticipants(group.getNumberOfParticipants());
        dto.setPricePerParticipant(group.getPricePerParticipant());
        dto.setRequiredQualifications(group.getRequiredQualifications());
        return dto;
    }
    public Group mapGroupPostDTOToGroup(GroupPostDTO dto) {
        Group group = new Group();
        GroupTemplate template = templateMapper.mapGroupTemplateGetDtoToGroupTemplate(dto.getTemplate());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setNotes(dto.getNotes());
        group.setNumberOfParticipants(dto.getNumberOfParticipants());
        group.setRequiredQualifications(dto.getRequiredQualifications() == null ? template.getRequiredQualifications() : dto.getRequiredQualifications());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setPricePerParticipant(dto.getPricePerParticipant());
        return group;
    }

    public Group mapGroupGetDTOToGroup(GroupGetDTO dto) {
        Group group = new Group();
        GroupTemplate template = templateMapper.mapGroupTemplateGetDtoToGroupTemplate(dto.getTemplate());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setNotes(dto.getNotes());
        group.setNumberOfParticipants(dto.getNumberOfParticipants());
        group.setRequiredQualifications(dto.getRequiredQualifications() == null ? template.getRequiredQualifications() : dto.getRequiredQualifications());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setPricePerParticipant(dto.getPricePerParticipant());
        return group;
    }
}
