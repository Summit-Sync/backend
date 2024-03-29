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
    private String title;//check
    private int numberOfDates;//check
    private String description;
    private int duration;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Qualification> requiredQualifications;
    private BigDecimal durationInMinutes;

    public BaseTemplate(String acronym, String title, int numberOfDates, String description, List<Qualification> requiredQualifications, int duration) {
        this.acronym = acronym;
        this.title = title;
        this.numberOfDates = numberOfDates;
        this.description = description;
        this.requiredQualifications = requiredQualifications;
        this.duration=duration;
    }
}
