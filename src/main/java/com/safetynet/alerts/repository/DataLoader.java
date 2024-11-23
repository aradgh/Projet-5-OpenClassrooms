package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

/**
 * Utility class for loading and saving application data from/to a JSON file.
 */
public class DataLoader {

    public static final String DATA_FILE = "data.json";
    private static final Logger logger = LogManager.getLogger(DataLoader.class);

    /**
     * Loads data from the JSON file into memory and persists it.
     *
     * @throws IOException If an error occurs while reading the file.
     */
    public static void loadData() throws IOException {
        logger.info("Starting data loading process from file: {}", DATA_FILE);

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Load data into memory
            Data.persons = mapper.readValue(new File(DATA_FILE), Data.class).getPersons();
            Data.firestations = mapper.readValue(new File(DATA_FILE), Data.class).getFirestations();
            Data.medicalrecords = mapper.readValue(new File(DATA_FILE), Data.class).getMedicalrecords();

            logger.debug("Data loaded into memory: \nPersons: {}\nFirestations: {}\nMedicalRecords: {}",
                Data.persons, Data.firestations, Data.medicalrecords);

            // Save data to ensure consistency
            saveData();
            logger.info("Data successfully loaded and saved from file: {}", DATA_FILE);
        } catch (IOException e) {
            logger.error("Error while loading data from file: {}", DATA_FILE, e);
            throw e;
        }
    }
}
