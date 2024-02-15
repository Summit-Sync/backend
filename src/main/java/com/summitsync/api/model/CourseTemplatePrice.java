package com.summitsync.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseTemplatePrice {
    @Id
    @GeneratedValue
    private long ctpId;
    private double price;
    private String category;
}
