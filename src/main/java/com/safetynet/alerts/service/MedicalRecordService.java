package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;

import java.util.List;

public class MedicalRecordService {
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> findAll() {
        return medicalRecordRepository.findAll();
    }

    public List<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return (List<MedicalRecord>) medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<MedicalRecord> findByBirthdate(String birthdate) {
        return (List<MedicalRecord>) medicalRecordRepository.findByBirthdate(birthdate);
    }

    public List<MedicalRecord> findByAllergies(String allergies) {
        return (List<MedicalRecord>) medicalRecordRepository.findByAllergies(allergies);
    }

    public List<MedicalRecord> findByMedications(String medications) {
        return (List<MedicalRecord>) medicalRecordRepository.findByMedications(medications);
    }

    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }
}
