package com.b2w.starwarsplanets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StarWarsPlanetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsPlanetsApplication.class, args);
	}

}
