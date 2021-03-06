package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({ "com.demo.controller", "com.demo.service", "com.demo.configuration", "com.demo.applicationListener","com.demo.scheduler" })
@EntityScan("com.demo.model")
@EnableMongoRepositories("com.demo.repository")
@EnableScheduling
public class SpringBootApacheKafaka {

	public static void main(String[] args) {

		SpringApplication.run(new Object[] { SpringBootApacheKafaka.class }, args);
	}
}