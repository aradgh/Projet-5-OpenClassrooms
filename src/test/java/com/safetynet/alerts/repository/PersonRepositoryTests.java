package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.safetynet.alerts.model.Data.persons;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonRepositoryTests {

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        persons = new ArrayList<>(); // Initialisation de la liste
        personRepository = new PersonRepository();
    }

    @Test
    void findById_ShouldReturnPerson_WhenIdExists() {
        // Arrange
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@email.com");
        persons.add(person);
        UUID id = person.getId();

        // Act
        Person result = personRepository.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(person, result);
    }

    @Test
    void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Act
        Person result = personRepository.findById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    void addPerson_ShouldAddPersonSuccessfully() throws IOException {
        // Arrange
        Person person = new Person("Jane", "Smith", "456 Elm St", "City", "54321", "987-654-3210", "jane.smith@email.com");

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            personRepository.addPerson(person);

            // Assert
            assertEquals(1, persons.size());
            assertEquals(person, persons.get(0));
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updatePerson_ShouldUpdatePersonSuccessfully() throws IOException {
        // Arrange
        Person person = new Person("Jane", "Smith", "456 Elm St", "City", "54321", "987-654-3210", "jane.smith@email.com");
        persons.add(person);

        Person updatedPerson = person.copy();
        updatedPerson.setEmail("new.email@example.com");

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            personRepository.updatePerson(updatedPerson);

            // Assert
            assertEquals(1, persons.size());
            assertEquals("new.email@example.com", persons.get(0).getEmail());
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void updatePerson_ShouldNotUpdate_WhenPersonDoesNotExist() throws IOException {
        // Arrange
        Person person = new Person("Nonexistent", "Person", "123 Fake St", "Nowhere", "00000", "000-000-0000", "fake@email.com");

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            personRepository.updatePerson(person);

            // Assert
            assertTrue(persons.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void deletePerson_ShouldDeletePersonSuccessfully() throws IOException {
        // Arrange
        Person person = new Person("Jane", "Smith", "456 Elm St", "City", "54321", "987-654-3210", "jane.smith@email.com");
        persons.add(person);

        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = personRepository.deletePerson(person.getId());

            // Assert
            assertTrue(isDeleted);
            assertTrue(persons.isEmpty());
            mockedSaveData.verify(JsonFileUtil::saveData, times(1));
        }
    }

    @Test
    void deletePerson_ShouldReturnFalse_WhenPersonDoesNotExist() throws IOException {
        // Arrange
        try (MockedStatic<JsonFileUtil> mockedSaveData = mockStatic(JsonFileUtil.class)) {
            // Act
            boolean isDeleted = personRepository.deletePerson(UUID.randomUUID());

            // Assert
            assertFalse(isDeleted);
            mockedSaveData.verify(JsonFileUtil::saveData, never());
        }
    }

    @Test
    void findByAddress_ShouldReturnMatchingPersons() {
        // Arrange
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@email.com");
        Person person2 = new Person("Jane", "Smith", "123 Main St", "City", "54321", "987-654-3210", "jane.smith@email.com");
        persons.addAll(List.of(person1, person2));

        // Act
        List<Person> result = personRepository.findByAddress("123 Main St");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByCity_ShouldReturnMatchingPersons() {
        // Arrange
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@email.com");
        Person person2 = new Person("Jane", "Smith", "456 Elm St", "City", "54321", "987-654-3210", "jane.smith@email.com");
        persons.addAll(List.of(person1, person2));

        // Act
        List<Person> result = personRepository.findByCity("City");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByLastName_ShouldReturnMatchingPersons() {
        // Arrange
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@email.com");
        persons.add(person1);

        // Act
        List<Person> result = personRepository.findByLastName("Doe");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
