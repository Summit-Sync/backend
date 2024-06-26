package com.summitsync.api.grouptemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface GroupTemplateRepository extends JpaRepository<GroupTemplate, Long> {
}
