package com.summitsync.api.group;

import com.summitsync.api.contact.ContactMapper;
import com.summitsync.api.contact.ContactService;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateMapper;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.group.dto.GroupGetDTO;
import com.summitsync.api.group.dto.GroupPostDTO;
import com.summitsync.api.grouptemplate.GroupTemplateMapper;
import com.summitsync.api.grouptemplate.GroupTemplateService;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainer.TrainerService;
import com.summitsync.api.trainer.dto.TrainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMapper {
    private final GroupTemplateMapper templateMapper;
    private final EventDateMapper eventDateMapper;
    private final QualificationMapper qualificationMapper;
    private final GroupTemplateService groupTemplateService;
    private final KeycloakRestService keycloakRestService;
    private final TrainerMapper trainerMapper;
    private final LocationMapper locationMapper;
    private final ContactMapper contactMapper;
    private final ContactService contactService;
    private final EventDateService eventDateService;
    private final LocationService locationService;
    private final QualificationService qualificationService;
    private final TrainerService trainerService;
    public GroupGetDTO mapGroupToGroupGetDto(Group group, String jwt) {
        Set<TrainerDto> trainerDtos = new HashSet<>();

        for (var trainer : group.getTrainers()) {
            var keycloakUser = this.keycloakRestService.getUser(trainer.getSubjectId(), jwt);
            var trainerDto = this.trainerMapper.mapKeycloakUserToTrainerDto(keycloakUser, trainer);
            trainerDtos.add(trainerDto);
        }

        return GroupGetDTO.builder()
                .id(group.getGroupId())
                .acronym(group.getAcronym())
                .groupNumber(group.getGroupNumber())
                .title(group.getTitle())
                .description(group.getDescription())
                .numberOfDates(group.getNumberOfDates())
                .duration(group.getDuration())
                .contact(this.contactMapper.mapContactToContactGetDto(group.getContact()))
                .dates(group.getDates().stream().map(EventDate::getStartTime).collect(Collectors.toSet()))
                .numberParticipants(group.getNumberParticipants())
                .location(this.locationMapper.mapLocationToGetLocationDto(group.getLocation()))
                .meetingPoint(group.getMeetingPoint())
                .trainerPricePerHour(group.getTrainerPricePerHour())
                .pricePerParticipant(group.getPricePerParticipant())
                .requiredQualifications(group.getQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).collect(Collectors.toSet()))
                .participantsPerTrainer(group.getParticipantsPerTrainer())
                .trainers(trainerDtos)
                .totalPrice(group.getTotalPrice())
                .build();

    }
    public Group mapGroupPostDTOToGroup(GroupPostDTO dto) {
        var dates = new ArrayList<EventDate>();
        for (var d : dto.getEvents()) {
           var newEventDate = EventDate.builder()
                   .startTime(d)
                   .durationInMinutes(dto.getDuration())
                   .build();

           var savedEventDate = this.eventDateService.create(newEventDate);
           dates.add(savedEventDate);
        }

        return Group.builder()
                .cancelled(false)
                .acronym(dto.getAcronym())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .numberOfDates(dto.getNumberOfDates())
                .duration(dto.getDuration())
                .contact(this.contactMapper.mapContactPostDtoToContact(dto.getContact()))
                .dates(dates)
                .numberParticipants(dto.getNumberParticipants())
                .location(this.locationService.getLocationById(dto.getLocation()))
                .meetingPoint(dto.getMeetingPoint())
                .trainerPricePerHour(dto.getTrainerPricePerHour())
                .pricePerParticipant(dto.getPricePerParticipant())
                .qualifications(dto.getRequiredQualifications().stream().map(this.qualificationService::findById).toList())
                .participantsPerTrainer(dto.getParticipantsPerTrainer())
                .trainers(dto.getTrainers().stream().map(this.trainerService::findById).toList())
                .totalPrice(new BigDecimal("0.0"))
                .build();
    }

}
