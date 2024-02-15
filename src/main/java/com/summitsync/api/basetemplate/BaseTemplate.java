package com.summitsync.api.basetemplate;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long baseTemplateId;
    private String acronym;
    private int numberOfDates;
    private String description;
    private int duration;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Qualification> requiredQualifications;
    private BigDecimal durationInMinutes;
}
