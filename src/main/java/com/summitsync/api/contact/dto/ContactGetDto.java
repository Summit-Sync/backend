package com.summitsync.api.contact.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactGetDto {
    private long contactId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
