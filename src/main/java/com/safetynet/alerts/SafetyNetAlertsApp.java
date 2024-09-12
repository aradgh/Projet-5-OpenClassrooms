package com.safetynet.alerts;

import com.safetynet.alerts.repository.DataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyNetAlertsApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SafetyNetAlertsApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DataLoader.loadData();
    }
}