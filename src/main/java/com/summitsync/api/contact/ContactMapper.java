package com.summitsync.api.contact;

import com.summitsync.api.contact.dto.ContactGetDto;
import com.summitsync.api.contact.dto.ContactPostDto;
import org.springframework.stereotype.Service;

@Service
public class ContactMapper {
    public Contact mapContactPostDtoToContact(ContactPostDto contactPostDto) {
        return Contact
                .builder()
                .firstName(contactPostDto.getFirstName())
                .lastName(contactPostDto.getLastName())
                .email(contactPostDto.getEmail())
                .telephone(contactPostDto.getTelephone())
                .build();
    }

    public ContactGetDto mapContactToContactGetDto(Contact contact) {
        return ContactGetDto
                .builder()
                .contactId(contact.getContactId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .telephone(contact.getTelephone())
                .build();
    }
}
