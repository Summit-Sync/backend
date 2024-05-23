package com.summitsync.api;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.summitsync.api.mail.GoogleCalendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@SpringBootApplication
public class SummitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitSyncApplication.class, args);
		DateTime startDateTime = new DateTime("2024-05-25T09:00:00-07:00");
		DateTime endDateTime = new DateTime("2024-05-25T17:00:00-07:00");
		EventDateTime startDate = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
		EventDateTime endDate = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Los_Angeles");
        try {
            GoogleCalendar.createEvent("Test title", startDate, endDate, "EK", "Hans Müller", "Hier noch Notizen");
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
