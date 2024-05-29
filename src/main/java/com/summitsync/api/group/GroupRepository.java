package com.summitsync.api.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByAcronymOrderByGroupNumberDesc(String acronym);
}
