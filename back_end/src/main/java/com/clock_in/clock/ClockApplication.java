package com.clock_in.clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClockApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClockApplication.class, args);
	}

}
