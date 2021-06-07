package com.example.nativemaven;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NativeMavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeMavenApplication.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return event -> System.out.println("Hello, world!");
	}
}
