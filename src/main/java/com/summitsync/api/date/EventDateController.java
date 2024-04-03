package com.summitsync.api.date;

import com.summitsync.api.date.dto.EventDateGetDto;
import com.summitsync.api.date.dto.EventDatePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/eventdate")
@RequiredArgsConstructor
public class EventDateController {

    private final EventDateService service;
    private final EventDateMapper mapper;

    @PostMapping
    public ResponseEntity<EventDateGetDto> createEventDate(@RequestBody EventDatePostDto dto) {
        EventDate eventDate = this.service.create(this.mapper.mapEventDatePostDtoToEventDate(dto));
        return new ResponseEntity<>(this.mapper.mapEventDateToEventDateGetDto(eventDate), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDateGetDto> getEventDateById(@PathVariable long id) {
        EventDate eventDate = this.service.findById(id);
        return new ResponseEntity<>(this.mapper.mapEventDateToEventDateGetDto(eventDate), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<EventDateGetDto>> getAllEventDates() {
        List<EventDate> all = this.service.getAll();
        List<EventDateGetDto> result = new ArrayList<>();
        for (EventDate e : all) {
            result.add(this.mapper.mapEventDateToEventDateGetDto(e));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventDateGetDto> deleteEventDateById(@PathVariable long id) {
        EventDate eventDate = this.service.findById(id);
        this.service.deleteById(id);
        return new ResponseEntity<>(this.mapper.mapEventDateToEventDateGetDto(eventDate), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDateGetDto> updateEventDate(@PathVariable long id, @RequestBody EventDatePostDto dto) {
        EventDate eventDate = this.service.update(id, dto);
        return new ResponseEntity<>(this.mapper.mapEventDateToEventDateGetDto(eventDate), HttpStatus.OK);
    }
}
