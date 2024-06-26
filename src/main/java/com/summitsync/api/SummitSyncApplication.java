package com.summitsync.api;

import com.summitsync.api.calendar.CalendarService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SummitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitSyncApplication.class, args);
	}

}
