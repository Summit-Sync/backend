package com.summitsync.api.date;

import com.summitsync.api.date.dto.EventDatePostDto;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventDateService {

    private final EventDateRepository repository;
    public EventDate create(EventDate eventDate) { return this.repository.save(eventDate); }

    public EventDate findById(long id) {
        Optional<EventDate> data = this.repository.findById(id);
        if (data.isEmpty()) {
            throw new RuntimeException("EventDate with id " + id + " does not exist.");
        }
        return data.get();
    }

    public void deleteById(long id) {
        this.repository.deleteById(id);
    }

    public List<EventDate> getAll() {
        return this.repository.findAll();
    }

    public EventDate update(long id, EventDatePostDto dto) {
        EventDate eventDate = this.findById(id);
        eventDate.setDurationInMinutes(dto.getDurationInMinutes());
        eventDate.setStartTime(dto.getStartTime());
        return eventDate;
    }
}
