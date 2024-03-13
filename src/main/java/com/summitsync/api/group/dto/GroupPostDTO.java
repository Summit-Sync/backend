package com.summitsync.api.group.dto;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class GroupPostDTO {
    private GroupTemplateGetDTO template;
    private String notes;
    private List<Integer> requiredQualificationIDs;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
}
