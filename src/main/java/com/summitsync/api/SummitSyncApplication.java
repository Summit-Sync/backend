package com.summitsync.api;

import com.summitsync.api.mail.Mailing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Session;

@SpringBootApplication
public class SummitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitSyncApplication.class, args);

	}
}
