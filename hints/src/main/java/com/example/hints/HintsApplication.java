package com.example.hints;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.ResourceHint;

@ResourceHint(patterns = "banner.txt") // 1.
@SpringBootApplication
public class HintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HintsApplication.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return event -> System.out.println("hello, world!");
	}
}


// 1. Resources
//
//

// 2. reflection
//
//

// need to demonstrate serialization
// need to demonstrate jdk proxies
// need to demonstrate spring proxies
// need to demonstrate