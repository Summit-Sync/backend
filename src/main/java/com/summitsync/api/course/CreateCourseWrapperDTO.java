package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.coursetemplate.dto.CourseTemplateDto;
import lombok.Data;

@Data
public class CreateCourseWrapperDTO {
    private CourseGetDTO course;
    private CourseTemplateDto template;
}
