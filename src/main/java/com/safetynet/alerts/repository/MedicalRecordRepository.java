package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.safetynet.alerts.repository.Data.medicalrecords;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

@Repository
public class MedicalRecordRepository {
    public List<MedicalRecord> findAll() {
        return medicalrecords;
    }

    public MedicalRecord findById(UUID medicalRecordId) {
        return medicalrecords.stream()
            .filter(medicalRecord -> medicalRecord.getId().equals(medicalRecordId))
            .findFirst()
            .orElse(null);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        medicalrecords.add(medicalRecord);
        saveData();
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        if (this.findById(medicalRecord.getId()) != null) {
            medicalrecords.set(medicalrecords.indexOf(this.findById(medicalRecord.getId())), medicalRecord);
            saveData();
        }
    }

    public boolean deleteMedicalRecord(UUID medicalRecordId) throws IOException {
        if (this.findById(medicalRecordId) != null) {
            medicalrecords.remove(this.findById(medicalRecordId));
            saveData();
            return true;
        }
        return false;
    }
}
