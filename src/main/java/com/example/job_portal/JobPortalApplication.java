package com.example.job_portal;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobPortalApplication {
	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));

		System.out.println("Mongo URI: " + System.getProperty("MONGODB_URI"));
		System.out.println("Gemini Key: " + System.getProperty("GEMINI_API_KEY"));

		SpringApplication.run(JobPortalApplication.class, args);
	}
}