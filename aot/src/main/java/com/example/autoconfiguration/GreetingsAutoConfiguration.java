package com.example.autoconfiguration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * comment out the @ConditionalOnClass and you'll see this configuration reflected in the
 * AOT-generated SpringFactories, but it won't end up in SpringFactories if you restore
 * the conditional, which assumes the presence of a type that isn't on the classpath
 *
 */
@Configuration
// @ConditionalOnClass(name =
// "org.springframework.jdbc.datasource.init.DatabasePopulator")
class GreetingsAutoConfiguration {

	@Bean
	ApplicationRunner runner() {
		return args -> System.out.println("hello, world!");
	}

}
