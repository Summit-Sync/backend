package com.summitsync.api.course;

public class CourseMapper {
    public Course mapCourseDTOToCourse(CourseDTO dto) {
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
        course.setNumberOfWaitList(dto.getTemplate().getNumberOfWaitList());
        course.setParticipants(dto.getParticipants());
        return course;
    }


    public CourseDTO mapCourseToCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setTemplate(course.getTemplate());
        dto.setDates(course.getDates());
        dto.setLocation(course.getLocation());
        dto.setParticipants(course.getParticipants());
        dto.setActualPrice(course.getActualPrice());
        dto.setDescription(course.getDescription());
        dto.setTitle(course.getTitle());
        dto.setRequiredQualifications(course.getRequiredQualifications());
        dto.setNotes(course.getNotes());
        return dto;
    }
}
