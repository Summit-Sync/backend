package com.summitsync.api.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseUpdateDTO extends CoursePostDTO{
    boolean finished;
    boolean cancelled;
}
