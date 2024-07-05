package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MedicalRecordRepository extends Repository<MedicalRecord> {

    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecord findByBirthdate(String birthdate);

    MedicalRecord findByAllergies(String allergies);

    MedicalRecord findByMedications(String medications);

    List<MedicalRecord> findAll();

    MedicalRecord save(MedicalRecord medicalRecord);
}
