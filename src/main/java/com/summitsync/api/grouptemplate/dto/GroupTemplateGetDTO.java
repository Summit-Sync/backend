package com.summitsync.api.grouptemplate.dto;

import com.summitsync.api.qualification.Qualification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTemplateGetDTO {
    private long id;
    private String title;
    private String acronym;
    private int trainerKey;
    private BigDecimal pricePerTrainerPerHour;
    private String description;
    private List<Qualification> requiredQualification;
}
