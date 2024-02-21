package com.summitsync.api.coursetemplate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTemplateRepository extends JpaRepository<CourseTemplate,Long> {

    CourseTemplate findCourseTemplateByBaseTemplateId(Long id);
}
