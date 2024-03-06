package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {
    public Course mapCourseGetDTOToCourse(CourseGetDTO dto) {
        Course course = new Course();
        course.setRequiredQualifications(dto.getRequiredQualifications());
        course.setDescription(dto.getDescription());
        course.setTitle(dto.getTitle());
        course.setNotes(dto.getNotes());
        course.setDates(dto.getDates());
        course.setActualPrice(dto.getActualPrice());
        course.setLocation(dto.getLocation());
        course.setNumberOfDates(dto.getTemplate().getNumberOfDates());
        course.setNumberOfParticipants(dto.getTemplate().getNumberOfParticipants());
        course.setNumberOfTrainers(dto.getTemplate().getNumberOfTrainers());
        course.setLengthOfWaitList(dto.getTemplate().getNumberOfWaitList());
        course.setParticipants(dto.getParticipants());
        course.setCourseId(dto.getId());
        return course;
    }


    public CourseGetDTO mapCourseToCourseGetDTO(Course course) {
        CourseGetDTO dto = new CourseGetDTO();
        dto.setTemplate(course.getTemplate());
        dto.setDates(course.getDates());
        dto.setLocation(course.getLocation());
        dto.setParticipants(course.getParticipants());
        dto.setActualPrice(course.getActualPrice());
        dto.setDescription(course.getDescription());
        dto.setTitle(course.getTitle());
        dto.setRequiredQualifications(course.getRequiredQualifications());
        dto.setNotes(course.getNotes());
        dto.setId(course.getCourseId());
        return dto;
    }
}
