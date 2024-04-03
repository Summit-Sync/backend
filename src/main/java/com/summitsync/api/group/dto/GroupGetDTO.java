package com.summitsync.api.group.dto;

import com.summitsync.api.date.dto.EventDateGetDto;
import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.qualification.Qualification;
import com.summitsync.api.qualification.dto.QualificationDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class GroupGetDTO {
    private long id;
    private GroupTemplateGetDTO template;
    private String notes;
    private Set<QualificationDto> requiredQualifications;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
    private List<EventDateGetDto> period;
}
