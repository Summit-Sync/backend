package com.summitsync.api.contact;

import com.summitsync.api.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact saveContact(Contact contact) {
        return this.contactRepository.save(contact);
    }

    public List<Contact> findAll() {
        return this.contactRepository.findAll();
    }

    public Contact findById(long id) {
        var contact = this.contactRepository.findById(id);

        if (contact.isEmpty()) {
            throw new ResourceNotFoundException("contact on id " + id + " not found");
        }

        return contact.get();
    }

    public void deleteById(long id) {
        this.contactRepository.deleteById(id);
    }
}
