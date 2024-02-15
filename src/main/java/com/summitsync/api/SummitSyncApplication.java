package com.summitsync.api;

import com.summitsync.api.person.Person;
import com.summitsync.api.person.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.NoSuchElementException;

@SpringBootApplication
public class SummitSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitSyncApplication.class, args);
	}
}
