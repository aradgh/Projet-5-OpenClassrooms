package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static com.safetynet.alerts.repository.DataLoader.DATA_FILE;

public class JsonFileUtil {
    public static void saveData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Crée une instance de la classe Data et met à jour les listes depuis les champs statiques de Data
        Data data = new Data();
        data.setPersons(Data.persons);
        data.setFirestations(Data.firestations);
        data.setMedicalrecords(Data.medicalrecords);

        // Écrit les données dans le fichier JSON
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), data);
    }
}
