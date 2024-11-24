package com.safetynet.alerts.service;

import com.safetynet.alerts.model.ChildAlertDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTests {

    private final PersonRepository personRepository = mock(PersonRepository.class);

    private final FirestationRepository firestationRepository = mock(FirestationRepository.class);

    private final MedicalRecordService medicalRecordService = mock(MedicalRecordService.class);

    private PersonService personService;

    private Person testPerson;
    private Firestation testFirestation;
    private MedicalRecord testMedicalRecord;

    @BeforeEach
    void setUp() {
        // Initialisation manuelle de la classe avec ses dépendances
        personService = new PersonService(personRepository, firestationRepository, medicalRecordService);
        // Person example
        testPerson = new Person(
            "John", "Doe", "123 Main St", "Springfield", "12345", "123-456-7890", "john.doe@example.com"
        );

        // Firestation example
        testFirestation = new Firestation("123 Main St", 1);

        // MedicalRecord example
        testMedicalRecord = new MedicalRecord("John", "Doe", "01/01/2010", List.of("Aspirin"), List.of("Pollen"));
    }

    @Test
    void addPerson_ShouldAddPersonSuccessfully() throws IOException {
        doNothing().when(personRepository).addPerson(testPerson);
        personService.addPerson(testPerson);
        verify(personRepository, times(1)).addPerson(testPerson);
    }

    @Test
    void updatePerson_ShouldUpdatePersonSuccessfully() throws IOException {
        // Arrange
        Person testPersonOriginal = testPerson.copy();
        when(personRepository.findById(testPerson.getId())).thenReturn(testPersonOriginal);

        // Modification de la personne
        testPerson.setEmail("new.email@example.com");

        // Act
        boolean isUpdated = personService.updatePerson(testPerson);

        // Assert
        assertTrue(isUpdated);

        // Capture l'argument utilisé pour updatePerson
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository, times(1)).updatePerson(captor.capture());

        // Vérifie que l'objet capturé correspond aux modifications attendues
        Person updatedPerson = captor.getValue();
        assertEquals("new.email@example.com", updatedPerson.getEmail());
        assertEquals(testPerson.getId(), updatedPerson.getId());
    }


    @Test
    void deletePerson_ShouldDeletePersonSuccessfully() throws IOException {
        when(personRepository.findById(testPerson.getId())).thenReturn(testPerson);
        when(personRepository.deletePerson(testPerson.getId())).thenReturn(true);

        boolean isDeleted = personService.deletePerson(testPerson.getId());

        assertTrue(isDeleted);
        verify(personRepository, times(1)).deletePerson(testPerson.getId());
    }

    @Test
    void getPersonsByFirestation_ShouldReturnListOfPersons() {
        when(firestationRepository.findByStation(1)).thenReturn(List.of(testFirestation));
        when(personRepository.findByAddress("123 Main St")).thenReturn(List.of(testPerson));

        List<Person> result = personService.getPersonsByFirestation(1);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testPerson, result.get(0));
    }

    @Test
    void getChildrenByAddress_ShouldReturnChildren() {
        when(personRepository.findByAddress("123 Main St")).thenReturn(List.of(testPerson));
        when(medicalRecordService.isChild(testPerson)).thenReturn(true);
        when(medicalRecordService.getMedicalRecordByPerson("John", "Doe")).thenReturn(testMedicalRecord);
        when(medicalRecordService.calculateAge(anyString())).thenCallRealMethod();

        Set<ChildAlertDTO> children = personService.getChildrenByAddress("123 Main St");

        assertNotNull(children);
        assertEquals(1, children.size());
        ChildAlertDTO child = children.iterator().next();
        assertEquals("John", child.getFirstName());
        assertEquals(14, child.getAge()); // Calcul basé sur la date de naissance simulée
    }

    @Test
    void getPhoneNumbersByFirestation_ShouldReturnPhoneNumbers() {
        when(firestationRepository.findByStation(1)).thenReturn(List.of(testFirestation));
        when(personRepository.findByAddress("123 Main St")).thenReturn(List.of(testPerson));

        Set<String> phoneNumbers = personService.getPhoneNumbersByFirestation(1);

        assertNotNull(phoneNumbers);
        assertEquals(1, phoneNumbers.size());
        assertTrue(phoneNumbers.contains(testPerson.getPhone()));
    }

    @Test
    void getEmailsByCity_ShouldReturnEmails() {
        // Stub pour "Springfield"
        when(personRepository.findByCity("Springfield")).thenReturn(List.of(testPerson));

        // Appel du service
        Set<String> emails = personService.getEmailsByCity("Springfield");

        // Vérifications
        assertNotNull(emails, "Emails should not be null");
        assertEquals(1, emails.size(), "Expected exactly 1 email");
        assertTrue(emails.contains(testPerson.getEmail()), "Email should match testPerson's email");

        // Vérification que la méthode mockée est appelée
        verify(personRepository, times(1)).findByCity("Springfield");
    }
}
