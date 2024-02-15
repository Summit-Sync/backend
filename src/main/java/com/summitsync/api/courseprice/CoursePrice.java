package com.summitsync.api.courseprice;

import com.summitsync.api.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_CoursePrice")
public class CoursePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long coursePriceId;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Course> course;
    private String category;
    private BigDecimal price;
}
