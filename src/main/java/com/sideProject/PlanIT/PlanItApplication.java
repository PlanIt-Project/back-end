package com.sideProject.PlanIT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class PlanItApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanItApplication.class, args);
	}

}
