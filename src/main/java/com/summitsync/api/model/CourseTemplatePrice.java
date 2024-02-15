package com.summitsync.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CourseTemplatePrice")
public class CourseTemplatePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseTemplateId;
    private BigDecimal price;
    private String category;
}
