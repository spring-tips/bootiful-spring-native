package com.example.autoconfiguration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
	* comment out the @ConditionalOnClass and you'll see this configuration
	* reflected in the AOT-generated SpringFactories, but as the class doesn't exist,
	* it won't endure if you reinstate the conditional.
	*
	*/
@Configuration
//@ConditionalOnClass(name = "org.springframework.jdbc.datasource.init.DatabasePopulator")
class GreetingsAutoConfiguration {

	@Bean
	ApplicationRunner runner() {
		return args -> System.out.println("hello, world!");
	}
}
