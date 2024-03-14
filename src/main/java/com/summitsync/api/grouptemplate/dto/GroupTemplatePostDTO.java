package com.summitsync.api.grouptemplate.dto;

import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupTemplatePostDTO {
    private String title;
    private String acronym;
    private int trainerKey;
    private BigDecimal pricePerTrainerPerHour;
    private String description;
    private List<Qualification> requiredQualification;
}
