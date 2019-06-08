package br.com.exmart.rtdpjlite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@EnableAutoConfiguration(exclude=FlywayAutoConfiguration.class)
@ActiveProfiles("test")
@ComponentScan(basePackages = {"br.com.exmart"})
@EntityScan(basePackages = {"br.com.exmart"})
@EnableJpaRepositories(basePackages="br.com.exmart")
public class Application {
	public static void main(String[] args) throws ClassNotFoundException {
		SpringApplication.run(Application.class, args);
	}
}
