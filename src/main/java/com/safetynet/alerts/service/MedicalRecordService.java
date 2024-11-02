package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public boolean updateMedicalRecord(MedicalRecord medicalRecordToUpdate) {
        Optional<MedicalRecord> existingMedicalRecordOpt = Optional.ofNullable(
            medicalRecordRepository.findById(medicalRecordToUpdate.getId()));

        return existingMedicalRecordOpt.map(existingMedicalRecord -> {
            boolean isUpdated = updateFirstNameIfNecessary(existingMedicalRecord, medicalRecordToUpdate.getFirstName());
            isUpdated |= updateLastNameIfNecessary(existingMedicalRecord, medicalRecordToUpdate.getLastName());
            isUpdated |= updateBirthdateIfNecessary(existingMedicalRecord, medicalRecordToUpdate.getBirthdate());
            isUpdated |= updateMedicationsIfNecessary(existingMedicalRecord, medicalRecordToUpdate.getMedications());
            isUpdated |= updateAllergiesIfNecessary(existingMedicalRecord, medicalRecordToUpdate.getAllergies());

            if (isUpdated) {
                try {
                    medicalRecordRepository.updateMedicalRecord(existingMedicalRecord);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return isUpdated;
        }).orElse(false);
    }

    public boolean deleteMedicalRecord(UUID medicalRecordId) throws IOException {
        return medicalRecordRepository.deleteMedicalRecord(medicalRecordId);
    }

    private boolean updateFirstNameIfNecessary(MedicalRecord existingRecord, String newFirstName) {
        if (newFirstName != null && !newFirstName.isEmpty() && !existingRecord.getFirstName().equals(newFirstName)) {
            existingRecord.setFirstName(newFirstName);
            return true;
        }
        return false;
    }

    private boolean updateLastNameIfNecessary(MedicalRecord existingRecord, String newLastName) {
        if (newLastName != null && !newLastName.isEmpty() && !existingRecord.getLastName().equals(newLastName)) {
            existingRecord.setLastName(newLastName);
            return true;
        }
        return false;
    }

    private boolean updateBirthdateIfNecessary(MedicalRecord existingRecord, String newBirthdate) {
        if (newBirthdate != null && !newBirthdate.isEmpty() && !existingRecord.getBirthdate().equals(newBirthdate)) {
            existingRecord.setBirthdate(newBirthdate);
            return true;
        }
        return false;
    }

    private boolean updateMedicationsIfNecessary(MedicalRecord existingRecord, List<String> newMedications) {
        if (newMedications != null && !newMedications.isEmpty() && !existingRecord.getMedications().equals(newMedications)) {
            existingRecord.setMedications(newMedications);
            return true;
        }
        return false;
    }

    private boolean updateAllergiesIfNecessary(MedicalRecord existingRecord, List<String> newAllergies) {
        if (newAllergies != null && !newAllergies.isEmpty() && !existingRecord.getAllergies().equals(newAllergies)) {
            existingRecord.setAllergies(newAllergies);
            return true;
        }
        return false;
    }

    public int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public boolean isChild(Person person) {
        MedicalRecord personRecord = getMedicalRecordByPerson(person.getFirstName(), person.getLastName());
        return personRecord != null && calculateAge(personRecord.getBirthdate()) <= 18;
    }

    public MedicalRecord getMedicalRecordByPerson(String firstName, String lastName) {
        return medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
    }

}
