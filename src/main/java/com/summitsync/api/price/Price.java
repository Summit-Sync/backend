package com.summitsync.api.price;

import com.summitsync.api.course.Course;
import com.summitsync.api.coursetemplate.CourseTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseTemplate> courseTemplates;
}
