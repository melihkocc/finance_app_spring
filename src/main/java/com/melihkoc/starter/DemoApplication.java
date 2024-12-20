package com.melihkoc.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.melihkoc"})
@EntityScan(basePackages = {"com.melihkoc.model"})
@ComponentScan(basePackages = {"com.melihkoc"})
@EnableJpaRepositories(basePackages = {"com.melihkoc.repository"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
