package com.summitsync.api.grouptemplate;

import com.summitsync.api.basetemplate.BaseTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_GroupTemplate")
public class GroupTemplate extends BaseTemplate {
    private BigDecimal pricePerTrainerPerHour;
    private int trainerKey;
}
