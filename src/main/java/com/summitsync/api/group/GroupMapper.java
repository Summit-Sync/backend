package com.summitsync.api.group;

import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMapper {
    private final GroupTemplateMapper templateMapper;
    private final QualificationService qualificationService;
    private final QualificationMapper qualificationMapper;
    public GroupGetDTO mapGroupToGroupGetDto(Group group) {
        GroupGetDTO dto = new GroupGetDTO();
        dto.setDescription(group.getDescription());
        dto.setNotes(group.getNotes());
        dto.setTemplate(this.templateMapper.mapGroupTemplateToGroupTemplateGetDTO(group.getTemplate()));
        dto.setNumberOfParticipants(group.getNumberOfParticipants());
        dto.setPricePerParticipant(group.getPricePerParticipant());
        dto.setRequiredQualifications(
                group.getRequiredQualifications()
                        .stream()
                        .map(this.qualificationMapper::mapQualificationToQualificationDto)
                        .collect(Collectors.toSet()
                        )
        );
        return dto;
    }
    public Group mapGroupPostDTOToGroup(GroupPostDTO dto) {
        Group group = new Group();
        GroupTemplate template = this.templateMapper.mapGroupTemplateGetDtoToGroupTemplate(dto.getTemplate());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setNotes(dto.getNotes());
        group.setNumberOfParticipants(dto.getNumberOfParticipants());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setPricePerParticipant(dto.getPricePerParticipant());
        return group;
    }

}
