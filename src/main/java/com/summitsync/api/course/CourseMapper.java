package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import com.summitsync.api.date.EventDateMapper;
import com.summitsync.api.keycloak.KeycloakRestService;
import com.summitsync.api.participant.ParticipantMapper;
import com.summitsync.api.participant.dto.ParticipantDto;
import com.summitsync.api.qualification.QualificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseMapper {
    private final EventDateMapper eventDateMapper;
    private final ParticipantMapper participantMapper;
    private final KeycloakRestService keycloakRestService;

    public CourseGetDTO mapCourseToCourseGetDTO(Course course, String jwt) {
        List<ParticipantDto> participantDtoList = new ArrayList<>();
        for (var p : course.getParticipants()) {
            var keycloakUser = this.keycloakRestService.getUser(p.getSubjectId(), jwt);
            participantDtoList.add(this.participantMapper.mapKeycloakUserToParticipantDto(keycloakUser, p));
        }

        CourseGetDTO dto = new CourseGetDTO();
        dto.setTemplate(course.getTemplate());
        dto.setDates(course.getDates().stream().map(this.eventDateMapper::mapEventDateToEventDateGetDto).toList());
        dto.setLocation(course.getLocation());
        dto.setParticipants(participantDtoList);
        dto.setActualPrice(course.getActualPrice());
        dto.setDescription(course.getDescription());
        dto.setTitle(course.getTitle());
        //dto.setRequiredQualifications(course.getRequiredQualifications());
        dto.setNotes(course.getNotes());
        dto.setId(course.getCourseId());
        return dto;
    }

    public Course mapCoursePostDTOToCourse(CoursePostDTO dto) {
        Course course = new Course();
        course.setDescription(dto.getDescription());
        course.setTitle(dto.getTitle());
        course.setNotes(dto.getNotes());
        course.setActualPrice(dto.getActualPrice());
        course.setLocation(dto.getLocation());
        course.setNumberOfDates(dto.getTemplate().getNumberOfDates());
        course.setNumberOfParticipants(dto.getTemplate().getNumberOfParticipants());
        course.setNumberOfTrainers(dto.getTemplate().getNumberOfTrainers());
        course.setLengthOfWaitList(dto.getTemplate().getNumberOfWaitList());
        return course;
    }
}
