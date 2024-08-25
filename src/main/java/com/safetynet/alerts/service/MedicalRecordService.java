package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public void addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        medicalRecordRepository.addMedicalRecord(medicalRecord);
    }

    public boolean updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (existingMedicalRecord != null) {
            existingMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            existingMedicalRecord.setMedications(medicalRecord.getMedications());
            existingMedicalRecord.setAllergies(medicalRecord.getAllergies());
            medicalRecordRepository.updateMedicalRecord(medicalRecord);
            return true;
        }
        return false;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) throws IOException {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (existingMedicalRecord != null) {
            medicalRecordRepository.deleteMedicalRecord(existingMedicalRecord.getFirstName(),
                existingMedicalRecord.getLastName());
            return true;
        }
        return false;
    }
}
