package com.summitsync.api.contact.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class ContactPostDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
