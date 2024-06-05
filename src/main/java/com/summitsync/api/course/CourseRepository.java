package com.summitsync.api.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByAcronymOrderByCourseNumberDesc(String acronym);
    @Query("SELECT c FROM Course c WHERE SIZE(c.trainers) < c.numberTrainer")
    List<Course> findAllWithMissingTrainers();
}
