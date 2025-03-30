package com.example.AquaSphere.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.AquaSphere.shopping",
		"com.AquaSphere.admin",
		"com.example.AquaSphere.Backend.Controller",
		"com.example.AquaSphere.Backend.Service",
		"com.example.AquaSphere.Backend.Repository",
		"com.example.AquaSphere.Backend.Scheduler",
		"com.example.AquaSphere.Backend",
})
@EnableScheduling
public class AquaSphereBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(AquaSphereBackendApplication.class, args);
	}

}


