package com.infybuzz.app;

import com.infybuzz.entity.Beverage;
import com.infybuzz.entity.BeverageType;
import com.infybuzz.repository.BeverageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan({"com.infybuzz.controller", "com.infybuzz.service"})
@EntityScan("com.infybuzz.entity")
@EnableJpaRepositories("com.infybuzz.repository")
@EnableEurekaClient
public class BeverageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeverageServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (BeverageRepository beverageRepository) {
		return args -> {

			List<Beverage> beverages = new ArrayList<>();
			beverages.add(new Beverage("Espresso", 12.50, BeverageType.COFFEE, 4, LocalDateTime.now(), null));
			beverages.add(new Beverage("Green Tea", 11.80, BeverageType.TEA, 8, LocalDateTime.now(), null));
			beverages.add(new Beverage("Cola", 13.50, BeverageType.SOFT_DRINKS, 5, LocalDateTime.now(), null));
			beverages.add(new Beverage("Apple Juice", 20.00, BeverageType.FRESH_JUICE, 5, LocalDateTime.now(), null));

			beverageRepository.saveAll(beverages);

		};
	}
}
