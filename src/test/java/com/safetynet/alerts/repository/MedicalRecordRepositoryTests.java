package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.safetynet.alerts.model.Data.medicalrecords;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordRepositoryTests {

    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    void setUp() {
        medicalrecords = new java.util.ArrayList<>(); // Initialisation de la liste statique
        medicalRecordRepository = new MedicalRecordRepository();
    }

    @Test
    void findAll_ShouldReturnAllMedicalRecords() {
        // Arrange
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));
        MedicalRecord record2 = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("med2"), List.of("allergy2"));
        medicalrecords.addAll(List.of(record1, record2));

        // Act
        List<MedicalRecord> result = medicalRecordRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById_ShouldReturnMedicalRecord_WhenIdExists() {
        // Arrange
        MedicalRecord record = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));
        medicalrecords.add(record);
        UUID id = record.getId();

        // Act
        MedicalRecord result = medicalRecordRepository.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(record, result);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Act
        MedicalRecord result = medicalRecordRepository.findById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    void addMedicalRecord_ShouldAddMedicalRecordSuccessfully() throws IOException {
        // Arrange
        MedicalRecord record = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("med2"), List.of("allergy2"));

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            medicalRecordRepository.addMedicalRecord(record);

            // Assert
            assertEquals(1, medicalrecords.size());
            assertEquals(record, medicalrecords.get(0));
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updateMedicalRecord_ShouldUpdateMedicalRecordSuccessfully() throws IOException {
        // Arrange
        MedicalRecord record = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("med2"), List.of("allergy2"));
        medicalrecords.add(record);

        MedicalRecord updatedRecord = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("newMed"), List.of("newAllergy"));
        updatedRecord.setId(record.getId());

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            medicalRecordRepository.updateMedicalRecord(updatedRecord);

            // Assert
            assertEquals(1, medicalrecords.size());
            assertEquals("newMed", medicalrecords.get(0).getMedications().get(0));
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updateMedicalRecord_ShouldNotUpdate_WhenMedicalRecordDoesNotExist() throws IOException {
        // Arrange
        MedicalRecord record = new MedicalRecord("Nonexistent", "Person", "01/01/1970", List.of(), List.of());

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            medicalRecordRepository.updateMedicalRecord(record);

            // Assert
            assertTrue(medicalrecords.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void deleteMedicalRecord_ShouldDeleteMedicalRecordSuccessfully() throws IOException {
        // Arrange
        MedicalRecord record = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("med2"), List.of("allergy2"));
        medicalrecords.add(record);

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = medicalRecordRepository.deleteMedicalRecord(record.getId());

            // Assert
            assertTrue(isDeleted);
            assertTrue(medicalrecords.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void deleteMedicalRecord_ShouldReturnFalse_WhenMedicalRecordDoesNotExist() throws IOException {
        // Arrange
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = medicalRecordRepository.deleteMedicalRecord(UUID.randomUUID());

            // Assert
            assertFalse(isDeleted);
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void findByFirstNameAndLastName_ShouldReturnMatchingMedicalRecord() {
        // Arrange
        MedicalRecord record = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));
        medicalrecords.add(record);

        // Act
        Optional<MedicalRecord> result = Optional.ofNullable(
            medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(record, result.get());
    }

    @Test
    void findByFirstNameAndLastName_ShouldReturnEmptyOptional_WhenNoMatch() {
        // Act
        Optional<MedicalRecord> result = Optional.ofNullable(
            medicalRecordRepository.findByFirstNameAndLastName("Nonexistent", "Person"));

        // Assert
        assertTrue(result.isEmpty());
    }
}

