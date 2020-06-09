package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({ "com.demo.controller", "com.demo.service" ,"com.demo.configuration"})
@EntityScan("com.demo.model")
@EnableMongoRepositories("com.demo.repository")
public class SpringBootHelloWorldApplication {

	public static void main(String[] args) {

		SpringApplication.run(new Object[] { SpringBootHelloWorldApplication.class }, args);
	}
}