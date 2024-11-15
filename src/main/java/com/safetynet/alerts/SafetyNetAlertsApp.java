package com.safetynet.alerts;

import com.safetynet.alerts.repository.DataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for SafetyNet Alerts.
 * Handles the initialization and loading of application data at startup.
 */
@SpringBootApplication
public class SafetyNetAlertsApp implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(SafetyNetAlertsApp.class);

    /**
     * Main entry point for the Spring Boot application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        logger.info("Starting SafetyNet Alerts Application...");
        SpringApplication.run(SafetyNetAlertsApp.class, args);
        logger.info("SafetyNet Alerts Application started successfully.");
    }

    /**
     * Runs during the application startup.
     * This method loads initial data from the JSON file into memory.
     *
     * @param args Command-line arguments.
     * @throws Exception If an error occurs while loading the data.
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing data loading process...");

        try {
            DataLoader.loadData();
            logger.info("Data loaded successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during data loading:", e);
            throw e; // Re-throw to terminate the application if critical data loading fails
        }
    }
}
