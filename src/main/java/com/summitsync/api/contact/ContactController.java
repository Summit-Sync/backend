package com.summitsync.api.contact;

import com.summitsync.api.contact.dto.ContactGetDto;
import com.summitsync.api.contact.dto.ContactPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final ContactMapper contactMapper;

    @PostMapping
    public ContactGetDto newContact(@RequestBody ContactPostDto contactPostDto) {
        var contact = this.contactMapper.mapContactPostDtoToContact(contactPostDto);
        var savedContact = this.contactService.saveContact(contact);
        return this.contactMapper.mapContactToContactGetDto(savedContact);
    }

    @GetMapping
    public List<ContactGetDto> getAllContacts() {
        return this.contactService
                .findAll()
                .stream()
                .map(this.contactMapper::mapContactToContactGetDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ContactGetDto getContactById(@PathVariable long id) {
        var contact = this.contactService.findById(id);

        return this.contactMapper.mapContactToContactGetDto(contact);
    }

    @DeleteMapping("/{id}")
    public void deleteContactById(@PathVariable long id) {
        this.contactService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ContactGetDto updateContact(@PathVariable long id, @RequestBody ContactPostDto contactPostDto) {
        var contact = this.contactService.findById(id);

        var updatedContact = this.contactService.updateContact(contact, contactPostDto);

        return this.contactMapper.mapContactToContactGetDto(updatedContact);
    }
}
