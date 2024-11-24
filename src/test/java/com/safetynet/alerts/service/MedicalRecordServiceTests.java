package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceTests {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private MedicalRecord testMedicalRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMedicalRecord = new MedicalRecord(
            "John", "Doe", "01/01/2010", List.of("med1", "med2"), List.of("allergy1")
        );
    }

    @Test
    void findAll_ShouldReturnAllMedicalRecords() {
        // Arrange
        when(medicalRecordRepository.findAll()).thenReturn(List.of(testMedicalRecord));

        // Act
        List<MedicalRecord> result = medicalRecordService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMedicalRecord, result.get(0));
    }

    @Test
    void findById_ShouldReturnMedicalRecord_WhenIdExists() {
        // Arrange
        UUID id = testMedicalRecord.getId();
        when(medicalRecordRepository.findById(id)).thenReturn(testMedicalRecord);

        // Act
        MedicalRecord result = medicalRecordService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicalRecord, result);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(medicalRecordRepository.findById(id)).thenReturn(null);

        // Act
        MedicalRecord result = medicalRecordService.findById(id);

        // Assert
        assertNull(result);
    }

    @Test
    void addMedicalRecord_ShouldAddRecordSuccessfully() throws IOException {
        // Act
        medicalRecordService.addMedicalRecord(testMedicalRecord);

        // Assert
        verify(medicalRecordRepository, times(1)).addMedicalRecord(testMedicalRecord);
    }

    @Test
    void updateMedicalRecord_ShouldUpdateRecordSuccessfully() throws IOException {
        // Arrange
        when(medicalRecordRepository.findById(testMedicalRecord.getId())).thenReturn(testMedicalRecord);

        MedicalRecord updatedRecord = new MedicalRecord(
            "John", "Doe", "01/01/2010", List.of("med3"), List.of("allergy2")
        );
        updatedRecord.setId(testMedicalRecord.getId());

        // Act
        boolean isUpdated = medicalRecordService.updateMedicalRecord(updatedRecord);

        // Assert
        assertTrue(isUpdated);
        ArgumentCaptor<MedicalRecord> captor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).updateMedicalRecord(captor.capture());
        MedicalRecord savedRecord = captor.getValue();
        assertEquals(List.of("med3"), savedRecord.getMedications());
        assertEquals(List.of("allergy2"), savedRecord.getAllergies());
    }

    @Test
    void deleteMedicalRecord_ShouldDeleteRecord_WhenIdExists() throws IOException {
        // Arrange
        UUID id = testMedicalRecord.getId();
        when(medicalRecordRepository.deleteMedicalRecord(id)).thenReturn(true);

        // Act
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(id);

        // Assert
        assertTrue(isDeleted);
        verify(medicalRecordRepository, times(1)).deleteMedicalRecord(id);
    }

    @Test
    void deleteMedicalRecord_ShouldReturnFalse_WhenIdDoesNotExist() throws IOException {
        // Arrange
        UUID id = UUID.randomUUID();
        when(medicalRecordRepository.deleteMedicalRecord(id)).thenReturn(false);

        // Act
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(id);

        // Assert
        assertFalse(isDeleted);
    }

    @Test
    void calculateAge_ShouldReturnCorrectAge() {
        // Act
        int age = medicalRecordService.calculateAge("01/01/2010");

        // Assert
        assertEquals(14, age); // Assuming the current year is 2023
    }

    @Test
    void isChild_ShouldReturnTrue_WhenAgeIsUnder18() {
        // Arrange
        Person child = new Person("Jane", "Doe", "123 Main St", "City", "12345", "123-456-7890", "jane.doe@email.com");
        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(testMedicalRecord);

        // Act
        boolean isChild = medicalRecordService.isChild(child);

        // Assert
        assertTrue(isChild);
    }

    @Test
    void isChild_ShouldReturnFalse_WhenAgeIsOver18() {
        // Arrange
        Person adult = new Person("Jane", "Doe", "123 Main St", "City", "12345", "123-456-7890", "jane.doe@email.com");
        MedicalRecord adultRecord = new MedicalRecord("Jane", "Doe", "01/01/2000", List.of(), List.of());
        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(adultRecord);

        // Act
        boolean isChild = medicalRecordService.isChild(adult);

        // Assert
        assertFalse(isChild);
    }

    @Test
    void getMedicalRecordByPerson_ShouldReturnMedicalRecord_WhenExists() {
        // Arrange
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(testMedicalRecord);

        // Act
        MedicalRecord result = medicalRecordService.getMedicalRecordByPerson("John", "Doe");

        // Assert
        assertNotNull(result);
        assertEquals(testMedicalRecord, result);
    }

    @Test
    void getMedicalRecordByPerson_ShouldReturnNull_WhenNotExists() {
        // Arrange
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(null);

        // Act
        MedicalRecord result = medicalRecordService.getMedicalRecordByPerson("John", "Doe");

        // Assert
        assertNull(result);
    }
}
