package com.summitsync.api.group;

import com.summitsync.api.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByAcronymOrderByGroupNumberDesc(String acronym);
    @Query("SELECT g FROM Group g WHERE SIZE(g.trainers) < g.numberParticipants/g.participantsPerTrainer")
    List<Group> findAllWithMissingTrainers();
}
