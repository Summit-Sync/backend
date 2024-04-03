package com.summitsync.api.group;

import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateMapper;
import com.summitsync.api.date.dto.EventDateGetDto;
import com.summitsync.api.date.dto.EventDatePostDto;
import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplate;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import com.summitsync.api.grouptemplate.GroupTemplateService;
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
    private final EventDateMapper eventDateMapper;
    private final QualificationMapper qualificationMapper;
    private final GroupTemplateService groupTemplateService;
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
        List<EventDateGetDto> period = new ArrayList<>();
        for (EventDate date : group.getPeriod()) {
            period.add(eventDateMapper.mapEventDateToEventDateGetDto(date));
        }
        dto.setPeriod(period);
        return dto;
    }
    public Group mapGroupPostDTOToGroup(GroupPostDTO dto) {
        Group group = new Group();
        var template = this.groupTemplateService.findById(dto.getTemplate());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setNotes(dto.getNotes());
        group.setNumberOfParticipants(dto.getNumberOfParticipants());
        group.setDescription(dto.getDescription() == null ? template.getDescription() : dto.getDescription());
        group.setPricePerParticipant(dto.getPricePerParticipant());
        List<EventDate> period = new ArrayList<>();
        for (EventDatePostDto date : dto.getPeriod()) {
            period.add(eventDateMapper.mapEventDatePostDtoToEventDate(date));
        }
        group.setPeriod(period);
        return group;
    }

}
