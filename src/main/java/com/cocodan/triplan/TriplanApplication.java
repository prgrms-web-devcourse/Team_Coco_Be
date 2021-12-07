package com.cocodan.triplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TriplanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriplanApplication.class, args);
	}

}
