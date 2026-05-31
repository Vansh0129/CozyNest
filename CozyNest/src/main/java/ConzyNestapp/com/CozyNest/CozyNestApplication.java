package ConzyNestapp.com.CozyNest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CozyNestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CozyNestApplication.class, args);

	}

}
