package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.save(medicalRecord);
    }

    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (existingMedicalRecord != null) {
            medicalRecordRepository.save(medicalRecord);
            return true;
        }
        return false;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        MedicalRecord existingMedicalRecord = medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (existingMedicalRecord != null) {
            medicalRecordRepository.delete(existingMedicalRecord);
            return true;
        }
        return false;
    }
}
