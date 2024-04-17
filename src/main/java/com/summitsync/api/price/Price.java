package com.summitsync.api.price;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CourseTemplatePrice")
@Builder
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long courseTemplatePriceId;
    private BigDecimal price;
    private String name;
}
