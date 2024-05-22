package com.summitsync.api.contact;

import com.summitsync.api.group.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "SS_Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long contactId;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    @OneToOne(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Group group;
}
