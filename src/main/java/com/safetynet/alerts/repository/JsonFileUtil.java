package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.safetynet.alerts.repository.DataLoader.DATA_FILE;

/**
 * Utility class for saving data to a JSON file.
 * This class handles serialization of application data into a JSON file format.
 */
public class JsonFileUtil {

    private static final Logger logger = LogManager.getLogger(JsonFileUtil.class);

    /**
     * Saves the current state of data (persons, firestations, medical records) into a JSON file.
     *
     * @throws IOException If an error occurs while writing to the file.
     */
    public static void saveData() throws IOException {
        logger.info("Starting the process of saving data to file: {}", DATA_FILE);

        ObjectMapper mapper = new ObjectMapper();

        // Create a Data object with current data in memory
        Data data = new Data();
        data.setPersons(Data.persons);
        data.setFirestations(Data.firestations);
        data.setMedicalrecords(Data.medicalrecords);

        try {
            // Write data to the JSON file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), data);
            logger.info("Data successfully saved to file: {}", DATA_FILE);
        } catch (IOException e) {
            logger.error("Error while saving data to file: {}", DATA_FILE, e);
            throw e; // Re-throw the exception for handling at a higher level
        }
    }
}
