package com.summitsync.api.group.dto;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import com.summitsync.api.grouptemplate.dto.GroupTemplatePostDTO;
import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupGetDTO {
    private long id;
    private GroupTemplateGetDTO template;
    private String notes;
    private List<Qualification> requiredQualifications;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
}
