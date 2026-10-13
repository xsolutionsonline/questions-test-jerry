package com.practice.clientapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.practice.clientapi.*"})
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
