package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

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
}
