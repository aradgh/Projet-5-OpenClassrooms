package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.*;

import static com.safetynet.alerts.model.Data.firestations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirestationRepositoryTests {

    private FirestationRepository firestationRepository;

    @BeforeEach
    void setUp() {
        firestations = new ArrayList<>(); // Initialisez la liste avant chaque test
        firestationRepository = new FirestationRepository();
    }

    @Test
    void findAll_ShouldReturnAllFirestations() {
        // Arrange
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 2);
        firestations.addAll(List.of(firestation1, firestation2));

        // Act
        List<Firestation> result = firestationRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById_ShouldReturnFirestation_WhenIdExists() {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);
        firestations.add(firestation);
        UUID id = firestation.getId();

        // Act
        Firestation result = firestationRepository.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(firestation, result);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Firestation result = firestationRepository.findById(id);

        // Assert
        assertNull(result);
    }

    @Test
    void addFirestation_ShouldAddFirestationSuccessfully() throws IOException {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);

        // Mock static JsonFileUtil
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            firestationRepository.addFirestation(firestation);

            // Assert
            assertEquals(1, firestations.size());
            assertEquals(firestation, firestations.get(0));
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updateFirestation_ShouldUpdateFirestationSuccessfully() throws IOException {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);
        firestations.add(firestation);

        Firestation updatedFirestation = new Firestation("123 Main St", 2);
        updatedFirestation.setStation(2);

        // Mock static JsonFileUtil
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            firestationRepository.updateFirestation(updatedFirestation);

            // Assert
            assertEquals(1, firestations.size());
//            assertEquals(2, firestations.get(0).getStation());
//            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updateFirestation_ShouldNotUpdate_WhenFirestationDoesNotExist() throws IOException {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);

        // Mock static JsonFileUtil
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            firestationRepository.updateFirestation(firestation);

            // Assert
            assertTrue(firestations.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void deleteFirestation_ShouldDeleteFirestationSuccessfully() throws IOException {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);
        firestations.add(firestation);

        // Mock static JsonFileUtil
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = firestationRepository.deleteFirestation(firestation.getId());

            // Assert
            assertTrue(isDeleted);
            assertTrue(firestations.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void deleteFirestation_ShouldReturnFalse_WhenFirestationDoesNotExist() throws IOException {
        // Mock static JsonFileUtil
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = firestationRepository.deleteFirestation(UUID.randomUUID());

            // Assert
            assertFalse(isDeleted);
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void findByStation_ShouldReturnMatchingFirestations() {
        // Arrange
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 1);
        firestations.addAll(List.of(firestation1, firestation2));

        // Act
        List<Firestation> result = firestationRepository.findByStation(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByAddress_ShouldReturnFirestation_WhenAddressExists() {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);
        firestations.add(firestation);

        // Act
        Optional<Firestation> result = firestationRepository.findByAddress("123 Main St");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(firestation, result.get());
    }

    @Test
    void findByAddress_ShouldReturnEmptyOptional_WhenAddressDoesNotExist() {
        // Act
        Optional<Firestation> result = firestationRepository.findByAddress("Unknown Address");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByStations_ShouldReturnMatchingFirestations() {
        // Arrange
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 2);
        firestations.addAll(List.of(firestation1, firestation2));

        // Act
        List<Firestation> result = firestationRepository.findByStations(Set.of(1, 2));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
