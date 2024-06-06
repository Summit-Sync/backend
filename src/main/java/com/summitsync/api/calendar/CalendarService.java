package com.summitsync.api.calendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.summitsync.api.course.CourseMapper;
import com.summitsync.api.course.CourseService;
import com.summitsync.api.group.GroupService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class CalendarService {
    private Calendar calendar;
    private static final String CREDENTIALS_FILE_PATH = "classpath:service-account.json";
    @Value("${summitsync.calendar.calendar-url}")
    private String calendarUrl;
    @Value("${summitsync.calendar.enabled}")
    private boolean enabled;
    private boolean initError = false;
    private final Logger log = LoggerFactory.getLogger(CalendarService.class);
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final CourseService courseService;
    private final GroupService groupService;

    public CalendarService(ResourceLoader resourceLoader, @Lazy CourseService courseService, @Lazy GroupService groupService) throws IOException, GeneralSecurityException {
        this.courseService = courseService;
        this.groupService = groupService;
        var resource = resourceLoader.getResource(CREDENTIALS_FILE_PATH);
        if (!resource.exists()) {
            log.warn("Unable to load credentials from {}. Disabling calendar", CREDENTIALS_FILE_PATH);
            this.initError = true;
            return;
        }
        var credentials = ServiceAccountCredentials.fromStream(resource.getInputStream())
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

        var requestInitializer = new HttpCredentialsAdapter(credentials);
        this.calendar = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer)
                .setApplicationName("Summit Sync")
                .build();
    }

    private boolean isEnabled() {
        return this.enabled && !this.initError;
    }

    public void updateEvent(String id, String summary, String description, String location) throws IOException {
        if (!this.isEnabled()) {
            logDisabled();
            return;
        }
        log.info("Updating event with id {}", id);


        var event = this.calendar.events().get(this.calendarUrl, id).execute();

        event
                .setSummary(summary)
                .setDescription(description)
                .setLocation(location);

        this.calendar.events().update(this.calendarUrl, id, event).execute();
    }

    public void createEvent(String id, String summary, String description, LocalDateTime startTime, LocalDateTime endTime, String location, String colorId) throws IOException {
        log.info("Creating a new calendar event from {} to {}", startTime, endTime);
        if (!this.isEnabled()) {
            logDisabled();
            return;
        }
        var event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setLocation(location)
                .setColorId(colorId)
                .setId(id);

        var start = this.localDateTimeToEventDateTime(startTime);
        var end = this.localDateTimeToEventDateTime(endTime);

        event.setStart(start);
        event.setEnd(end);
        this.calendar.events().insert(this.calendarUrl, event).execute();

    }

    public void deleteEvent(String id) throws IOException {
        if (!this.isEnabled()) {
            logDisabled();
            return;
        }

        this.calendar.events().delete(this.calendarUrl, id).execute();
    }

    private void logDisabled() {
        log.warn("Event creation is disabled.");
    }

    private EventDateTime localDateTimeToEventDateTime(LocalDateTime localDateTime, String tz) {
        var dateTime = new DateTime(localDateTime.atZone(ZoneId.of(tz)).toInstant().toEpochMilli());

        return new EventDateTime()
                .setDateTime(dateTime)
                .setTimeZone(tz);
    }

    private EventDateTime localDateTimeToEventDateTime(LocalDateTime localDateTime) {
        return this.localDateTimeToEventDateTime(localDateTime, "Europe/Berlin");
    }

    private void getEvent(String id) throws IOException {
        if (!this.isEnabled()) {
            return;
        }
        this.calendar.events().get(this.calendarUrl, id).execute();
    }

    public void execute(CalendarRequest<Event> request) throws IOException {
        request.execute();
    }

    @Scheduled(fixedDelay = 12, timeUnit = TimeUnit.HOURS)
    @Transactional
    protected void checkIntegrityScheduled() {
        this.checkIntegrity();
    }

    @Transactional
    public void checkIntegrity() {
        if (!this.isEnabled()) {
            return;
        }

        log.info("Checking integrity of the calendar...");
        var allCourses = this.courseService.getAll();
        var allGroups = this.groupService.getAll();

        for (var course : allCourses) {
            checkIntegrityFor(course);
        }

        for (var group: allGroups) {
            checkIntegrityFor(group);
        }

    }

    private void checkIntegrityFor(CalendarEvent event) {
        for (var occurrence: event.occurrences()) {
            var occurrenceId = event.occurrenceEventId(occurrence);
            try {
                this.getEvent(occurrenceId);
            } catch (IOException e) {
                log.warn("No calendar event found for id {}, date: {}", occurrenceId, occurrence.getStartTime());
            }
        }
    }

}
