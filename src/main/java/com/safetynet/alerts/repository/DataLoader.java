package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

public class DataLoader {
    public static final String DATA_FILE = "data.json";

    public static void loadData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Data.persons = mapper.readValue(new File(DATA_FILE), Data.class).getPersons();
        Data.firestations = mapper.readValue(new File(DATA_FILE), Data.class).getFirestations();
        Data.medicalrecords = mapper.readValue(new File(DATA_FILE), Data.class).getMedicalrecords();

        saveData();

        System.out.println("Data loaded successfully." + "\n" + Data.persons + "\n" + Data.firestations + "\n" + Data.medicalrecords);
    }
}
