package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.safetynet.alerts.repository.DataLoader.DATA_FILE;

public class JsonFileUtil {
    public static void saveData(List<Person> persons, List<Firestation> firestations, List<MedicalRecord> medicalRecords) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Data data = new Data(persons, firestations, medicalRecords);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), data);
    }
}
