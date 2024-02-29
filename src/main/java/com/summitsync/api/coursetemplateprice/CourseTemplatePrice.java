package com.summitsync.api.coursetemplateprice;

import com.summitsync.api.coursetemplate.CourseTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CourseTemplatePrice")
@Builder
public class CourseTemplatePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseTemplateId;
    private BigDecimal price;
    private String category;
}
