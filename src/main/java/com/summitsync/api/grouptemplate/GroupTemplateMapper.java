package com.summitsync.api.grouptemplate;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationRepository;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupTemplateMapper {
    private final QualificationMapper qualificationMapper;
    private final QualificationService qualificationService;
    private final LocationMapper locationMapper;
    private final LocationService locationService;
    public GroupTemplate mapGroupPostDtoToGroupTemplate(GroupTemplatePostDTO dto) {
        var location = locationService.getLocationById(dto.getLocation());
        var qualifications = dto.getRequiredQualificationList()
                .stream()
                .map(this.qualificationService::findById)
                .collect(Collectors.toSet());

        return GroupTemplate.builder()
                .acronym(dto.getAcronym())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .numberOfDates(dto.getNumberOfDates())
                .duration(dto.getDuration())
                .location(location)
                .meetingPoint(dto.getMeetingPoint())
                .trainerPricerPerHour(dto.getTrainerPricePerHour())
                .requiredQualifications(qualifications)
                .participantsPerTrainer(dto.getParticipantsPerTrainer())
                .build();
    }

    public GroupTemplateGetDTO mapGroupTemplateToGroupTemplateGetDTO(GroupTemplate template) {
        return GroupTemplateGetDTO.builder()
                .id(template.getGroupTemplateId())
                .acronym(template.getAcronym())
                .title(template.getTitle())
                .description(template.getDescription())
                .numberOfDates(template.getNumberOfDates())
                .duration(template.getDuration())
                .location(this.locationMapper.mapLocationToGetLocationDto(template.getLocation()))
                .meetingPoint(template.getMeetingPoint())
                .trainerPricePerHour(template.getTrainerPricerPerHour())
                .pricePerParticipant(template.getPricePerParticipant())
                .requiredQualificationList(template.getRequiredQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).collect(Collectors.toSet()))
                .participantsPerTrainer(template.getParticipantsPerTrainer())
                .build();
    }
}
