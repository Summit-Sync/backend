package com.summitsync.api.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface GroupRepository extends JpaRepository<Group, Long> {
}
