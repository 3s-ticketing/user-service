package org.ticketing.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(
	exclude = JpaRepositoriesAutoConfiguration.class
)
@ComponentScan(
	basePackages = {
		"org.ticketing.user",
		"org.ticketing.config",
		"org.ticketing.common.exception",
		"org.ticketing.common.filter",
		"org.ticketing.common.util"
	},
	excludeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = org.ticketing.config.security.SecurityConfig.class
		)
	}
)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
