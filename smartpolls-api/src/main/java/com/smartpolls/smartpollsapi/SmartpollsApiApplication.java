package com.smartpolls.smartpollsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SmartpollsApiApplication {

	public static void main(String[] args) {
		// Use IANA timezone name so PostgreSQL accepts it (rejects legacy "Asia/Calcutta")
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(SmartpollsApiApplication.class, args);
	}

}
