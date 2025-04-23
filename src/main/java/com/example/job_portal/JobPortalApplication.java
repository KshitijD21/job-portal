package com.example.job_portal;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobPortalApplication {
    public static void main(String[] args) {

        if (System.getenv("RENDER") == null) {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
            System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
        }
        SpringApplication.run(JobPortalApplication.class, args);
    }
}
