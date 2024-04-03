package com.summitsync.api.grouptemplate;

import com.summitsync.api.basetemplate.BaseTemplate;
import com.summitsync.api.contact.Contact;
import com.summitsync.api.qualification.Qualification;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SS_GroupTemplate")
public class GroupTemplate extends BaseTemplate {
    private BigDecimal pricePerTrainerPerHour;
    private int trainerKey;
    @OneToOne(fetch = FetchType.LAZY)
    private Contact contact;
}
