package com.summitsync.api.calendar;

import com.google.api.services.calendar.model.Event;
import com.summitsync.api.keycloak.KeycloakRestService;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public interface CalendarEvent {
    List<CalendarEventDateSet> occurrences();
    String eventDescription(CalendarEventDateSet calendarEventDateSet);
    String eventTitle(CalendarEventDateSet calendarEventDateSet, KeycloakRestService keycloakRestService);
    long eventId();
    String eventIdPrefix();
    String eventLocation();
    String eventColorId();

    default String occurrenceEventId(CalendarEventDateSet calendarEventDateSet) {
        var timestamp = calendarEventDateSet.getStartTime().atZone(ZoneId.of("Europe/Berlin")).toEpochSecond();
        return String.format("%s%05d%s", this.eventIdPrefix(), this.eventId(), timestamp);
    }

    default String finalDescription(String description) {
        return description + "\n\n=========\nDieser Kalendereintrag wurde automatisch von Summit Sync erstellt. Bitte NICHT manuell anpassen.";
    }

    default void createEvents(CalendarService calendarService, KeycloakRestService keycloakRestService) throws IOException {
        for (var occurrence: occurrences()) {
            this.createEvent(occurrence, calendarService, keycloakRestService);
        }
    }

    default void createEvent(CalendarEventDateSet occurrence, CalendarService calendarService, KeycloakRestService keycloakRestService) throws IOException {
        var id = occurrenceEventId(occurrence);
        calendarService.createEvent(
                id,
                this.eventTitle(occurrence, keycloakRestService),
                this.finalDescription(this.eventDescription(occurrence)),
                occurrence.getStartTime(),
                occurrence.getEndTime(),
                this.eventLocation(),
                this.eventColorId()
        );
    }

    default void occurrencesModification(List<CalendarEventDateSet> oldOccurrences, List<CalendarEventDateSet> newOccurrences, CalendarService calendarService, KeycloakRestService keycloakRestService) throws IOException {
        for (var oldOccurrence: oldOccurrences) {
            if (!newOccurrences.remove(oldOccurrence)) {
                calendarService.deleteEvent(this.occurrenceEventId(oldOccurrence));
            }
        }

        for (var newOccurrence: newOccurrences) {
            this.createEvent(newOccurrence, calendarService, keycloakRestService);
        }
    }

    default void deleteEvents(CalendarService calendarService) throws IOException {
        for (var occurrence: this.occurrences()) {
            calendarService.deleteEvent(this.occurrenceEventId(occurrence));
        }
    }

    default void updateEvents(CalendarService calendarService, KeycloakRestService keycloakRestService) throws IOException {
        for (var occurrence: this.occurrences()) {
            this.updateEvent(occurrence, calendarService, keycloakRestService);
        }
    }

    default void updateEvent(CalendarEventDateSet calendarEvent,CalendarService calendarService, KeycloakRestService keycloakRestService) throws IOException {
        var id = occurrenceEventId(calendarEvent);
        calendarService.updateEvent(
                id,
                this.eventTitle(calendarEvent, keycloakRestService),
                this.finalDescription(this.eventDescription(calendarEvent)),
                this.eventLocation()
        );

    }
}
