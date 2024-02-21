package com.summitsync.api.grouptemplate;

import com.summitsync.api.qualification.Qualification;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GroupTemplateDTO {
    private String title;
    private String acronym;
    private int trainerKey;
    private BigDecimal pricePerTrainerPerHour;
    private String description;
    private List<Qualification> requiredQualification;
    private long id;
}
