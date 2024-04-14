package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.date.EventDate;
import com.summitsync.api.date.EventDateMapper;
import com.summitsync.api.date.EventDateService;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.location.LocationMapper;
import com.summitsync.api.location.LocationService;
import com.summitsync.api.participant.ParticipantMapper;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.qualification.QualificationMapper;
import com.summitsync.api.qualification.QualificationService;
import com.summitsync.api.trainer.TrainerMapper;
import com.summitsync.api.trainer.TrainerService;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseMapper {
    private final ParticipantMapper participantMapper;
    private final LocationMapper locationMapper;
    private final QualificationMapper qualificationMapper;
    private final TrainerMapper trainerMapper;
    private final EventDateService eventDateService;
    private final LocationService locationService;
    private final QualificationService qualificationService;

    public CourseGetDTO mapCourseToCourseGetDTO(Course course, String jwt) {

        return CourseGetDTO.builder()
                .id(course.getCourseId())
                .visible(course.isVisible())
                .canceled(course.isCancelled())
                .finished(course.isFinished())
                .courseNumber(course.getCourseNumber())
                .acronym(course.getAcronym())
                .description(course.getDescription())
                .dates(course.getDates().stream().map(EventDate::getStartTime).toList())
                .duration(course.getDuration())
                .numberParticipants(course.getNumberParticipants())
                .participants(course.getParticipants().stream().map(participant -> this.participantMapper.mapParticipantToParticipantDto(participant, jwt)).toList())
                .waitList(course.getWaitList().stream().map(participant -> this.participantMapper.mapParticipantToParticipantDto(participant, jwt)).toList())
                .numberWaitlist(course.getNumberWaitlist())
                // TODO coursePrices
                .location(this.locationMapper.mapLocationToGetLocationDto(course.getLocation()))
                .meetingPoint(course.getMeetingPoint())
                .requiredQualifications(course.getRequiredQualifications().stream().map(this.qualificationMapper::mapQualificationToQualificationDto).toList())
                .numberTrainers(course.getNumberTrainer())
                .trainerList(course.getTrainers().stream().map(trainer -> this.trainerMapper.mapTrainerToTrainerDto(trainer, jwt)).toList())
                .notes(course.getNotes())
                .build();
    }

    public Course mapCoursePostDTOToCourse(CoursePostDTO dto) {
        var dates = new HashSet<EventDate>();
        if (dto.getDates() != null) {
            for (var date: dto.getDates()) {
                var eventDate = new EventDate();
                eventDate.setDurationInMinutes(dto.getDuration());
                eventDate.setStartTime(date);
                var dbDate = this.eventDateService.create(eventDate);
                dates.add(dbDate);
            }
        }

        return Course.builder()
                .visible(false)
                .cancelled(false)
                .finished(false)
                .courseNumber("1")
                .acronym(dto.getAcronym())
                .description(dto.getDescription())
                .dates(dates)
                .duration(dto.getDuration())
                .numberParticipants(dto.getNumberParticipants())
                .participants(new HashSet<>())
                .waitList(new HashSet<>())
                .numberWaitlist(dto.getNumberWaitlist())
                // TODO coursePrices
                .location(this.locationService.getLocationById(dto.getLocation()))
                .meetingPoint(dto.getMeetingPoint())
                .requiredQualifications(dto.getRequiredQualifications().stream().map(this.qualificationService::findById).collect(Collectors.toSet()))
                .numberTrainer(dto.getNumberTrainers())
                .trainers(new HashSet<>())
                .build();
    }
}
