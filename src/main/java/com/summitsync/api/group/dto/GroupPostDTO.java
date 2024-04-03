package com.summitsync.api.group.dto;

import com.summitsync.api.grouptemplate.dto.GroupTemplateGetDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class GroupPostDTO {
    private long template;
    private String notes;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
}
