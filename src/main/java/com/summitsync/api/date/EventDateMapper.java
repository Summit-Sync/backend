package com.summitsync.api.date;

import com.summitsync.api.date.dto.EventDateGetDto;
import com.summitsync.api.date.dto.EventDatePostDto;
import org.springframework.stereotype.Service;

@Service
public class EventDateMapper {
    public EventDate mapEventDatePostDtoToEventDate(EventDatePostDto dto) {
        EventDate eventDate = new EventDate();
        eventDate.setStartTime(dto.getStartTime());
        eventDate.setDurationInMinutes(dto.getDurationInMinutes());
        return eventDate;
    }

    public EventDateGetDto mapEventDateToEventDateGetDto(EventDate eventDate) {
        EventDateGetDto dto = new EventDateGetDto();
        dto.setEventDateId(eventDate.getEventDateId());
        dto.setStartTime(eventDate.getStartTime());
        dto.setDurationInMinutes(eventDate.getDurationInMinutes());
        return dto;
    }
}
