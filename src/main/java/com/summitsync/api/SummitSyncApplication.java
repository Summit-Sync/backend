package com.summitsync.api;

import com.summitsync.api.mail.MailDetail;
import com.summitsync.api.mail.MailService;
import com.summitsync.api.mail.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class SummitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitSyncApplication.class, args);

	}


}
