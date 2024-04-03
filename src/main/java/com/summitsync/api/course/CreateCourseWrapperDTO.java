package com.summitsync.api.course;

import com.summitsync.api.course.dto.CourseGetDTO;
import com.summitsync.api.course.dto.CoursePostDTO;
import lombok.Data;

@Data
public class CreateCourseWrapperDTO {
    private CoursePostDTO course;
    private long templateId;
}
