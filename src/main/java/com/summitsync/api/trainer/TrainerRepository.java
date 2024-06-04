package com.summitsync.api.trainer;

import com.summitsync.api.qualification.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findBySubjectId(String subjectId);
    @Query(value = "SELECT t, c FROM Trainer t, Course c JOIN c.dates d WHERE d.startTime BETWEEN :startDate AND :endDate AND SIZE(c.trainers) < c.numberTrainer AND c.requiredQualifications IN (SELECT q FROM Qualification q WHERE q MEMBER OF t.qualifications)", nativeQuery = true)
    List<Object[]> findTrainersWithAllCourses(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT t, g FROM Trainer t, Group g JOIN g.dates d WHERE d.startTime BETWEEN  :startDate AND :endDate AND (g.numberParticipants / g.participantsPerTrainer) > SIZE(g.trainers) AND g.requiredQualifications IN ((SELECT q FROM Qualification q WHERE q MEMBER OF t.qualifications)", nativeQuery = true)
    List<Object[]> findTrainersWithAllGroups(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
