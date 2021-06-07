package bootiful.nativex;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BuildpacksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildpacksApplication.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return args -> System.out.println("hello, world!");
	}
}
