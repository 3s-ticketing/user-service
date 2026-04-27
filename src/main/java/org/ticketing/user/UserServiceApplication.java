package org.ticketing.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@SpringBootApplication(
	scanBasePackages = {
		"org.ticketing.user",
		"org.ticketing.config",
		"org.ticketing.common.exception",
		"org.ticketing.common.filter",
		"org.ticketing.common.util"
	},
	exclude = JpaRepositoriesAutoConfiguration.class
)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
