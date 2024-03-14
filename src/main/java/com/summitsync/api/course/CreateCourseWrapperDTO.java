package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import lombok.Data;

@Data
public class CreateCourseWrapperDTO {
    private CourseGetDTO course;
    private long templateId;
}
