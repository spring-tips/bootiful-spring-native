package bootiful;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;

@Configuration
class BootifulCsvAnalyser {

	@Bean
	ApplicationRunner airlineSafetyApplicationRunner() {
		return args -> {
			var a = "data/airline";
			var b = "-safety.csv";
			var url = new ClassPathResource(a + b);
			try (var in = new InputStreamReader(url.getInputStream())) {
				var contents = FileCopyUtils.copyToString(in);
				var lines = contents.split("\r");
				System.out.println("there are " + lines.length + " lines");
			}
			;
		};
	}

}
