package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        medicalRecordRepository.addMedicalRecord(medicalRecord);
    }

    public boolean updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId());
        if (existingMedicalRecord != null) {
            existingMedicalRecord.setFirstName(medicalRecord.getFirstName());
            existingMedicalRecord.setLastName(medicalRecord.getLastName());
            existingMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            existingMedicalRecord.setMedications(medicalRecord.getMedications());
            existingMedicalRecord.setAllergies(medicalRecord.getAllergies());
            medicalRecordRepository.updateMedicalRecord(medicalRecord);
            return true;
        }
        return false;
    }

    public boolean deleteMedicalRecord(UUID medicalRecordId) throws IOException {
        return medicalRecordRepository.deleteMedicalRecord(medicalRecordId);
    }
}
