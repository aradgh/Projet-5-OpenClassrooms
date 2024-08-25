package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class DataLoader {
    public static final String DATA_FILE = "data.json";

    public static void loadData(PersonRepository personRepo, FirestationRepository firestationRepo, MedicalRecordRepository medicalRecordRepo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Data data = mapper.readValue(new File(DATA_FILE), Data.class);

        personRepo = new PersonRepository(data.getPersons());
        firestationRepo = new FirestationRepository(data.getFirestations());
        medicalRecordRepo = new MedicalRecordRepository(data.getMedicalRecords());
    }
}
