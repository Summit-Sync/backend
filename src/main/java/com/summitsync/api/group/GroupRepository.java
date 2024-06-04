package com.summitsync.api.group;

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
    @Query("SELECT g FROM Group g JOIN g.dates d WHERE (g.numberParticipants / g.participantsPerTrainer) > SIZE(g.trainers) AND d.startTime BETWEEN :startDate AND :endDate")
    List<Group> findGroupsWithMoreParticipantsPerTrainerThanTrainersAndDateIn(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
