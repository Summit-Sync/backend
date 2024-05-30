package com.summitsync.api.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByAcronymOrderByCourseNumberDesc(String acronym);
}
