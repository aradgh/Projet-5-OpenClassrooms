package com.safetynet.alerts;

import com.safetynet.alerts.repository.DataLoader;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyNetAlertsApp implements CommandLineRunner {

    private final PersonRepository personRepo;
    private final FirestationRepository firestationRepo;
    private final MedicalRecordRepository medicalRecordRepo;

    public SafetyNetAlertsApp(PersonRepository personRepo, FirestationRepository firestationRepo, MedicalRecordRepository medicalRecordRepo) {
        this.personRepo = personRepo;
        this.firestationRepo = firestationRepo;
        this.medicalRecordRepo = medicalRecordRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(SafetyNetAlertsApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DataLoader.loadData(personRepo, firestationRepo, medicalRecordRepo);
    }
}