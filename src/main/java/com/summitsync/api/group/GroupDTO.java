package com.summitsync.api.group;

import com.summitsync.api.grouptemplate.GroupTemplateDTO;
import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupDTO {
    private GroupTemplateDTO template;
    private String notes;
    private List<Qualification> requiredQualifications;
    private BigDecimal pricePerParticipant;
    private Integer numberOfParticipants;
    private String description;
    private long id;
}
