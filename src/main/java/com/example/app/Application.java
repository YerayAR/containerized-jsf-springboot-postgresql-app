package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        log.info("=================================================");
        log.info("STARTING JSF SPRING BOOT POSTGRESQL APPLICATION");
        log.info("=================================================");
        
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            log.error("FATAL ERROR DURING APPLICATION STARTUP", e);
            System.exit(1);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("=================================================");
        log.info("APPLICATION STARTED SUCCESSFULLY");
        log.info("=================================================");
        log.info("Server is ready to accept requests");
        log.info("Access the application at: http://localhost:8080");
        log.info("Health check endpoint: http://localhost:8080/actuator/health");
    }
}
