package com.summitsync.api.group.dto;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class GroupPostDTO {
    private GroupTemplateGetDTO template;
    private String notes;
    private List<Qualification> requiredQualifications;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
}
