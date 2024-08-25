package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MedicalRecordRepository {
    private Map<String, MedicalRecord> medicalRecords = new HashMap<>();

    public MedicalRecordRepository(List<MedicalRecord> medicalRecords) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            String key = medicalRecord.getFirstName() + medicalRecord.getLastName();
            this.medicalRecords.put(key, medicalRecord);
        }
    }

    public List<MedicalRecord> findAll() {
        return new ArrayList<>(medicalRecords.values());
    }

    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.get(firstName + lastName);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        String key = medicalRecord.getFirstName() + medicalRecord.getLastName();
        medicalRecords.put(key, medicalRecord);
        saveToFile();
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        String key = medicalRecord.getFirstName() + medicalRecord.getLastName();
        if (medicalRecords.containsKey(key)) {
            medicalRecords.put(key, medicalRecord);
            saveToFile();
        }
    }

    public void deleteMedicalRecord(String firstName, String lastName) throws IOException {
        String key = firstName + lastName;
        if (medicalRecords.containsKey(key)) {
            medicalRecords.remove(key);
            saveToFile();
        }
    }

    private void saveToFile() throws IOException {
        JsonFileUtil.saveData(null, null, findAll());
    }
}
