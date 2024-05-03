package de.htw.ai.traffic.bikelintrafficservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BikelinTrafficServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikelinTrafficServiceApplication.class, args);
	}
}
