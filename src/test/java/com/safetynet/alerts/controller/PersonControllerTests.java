package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.ChildAlertDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonInfoDTO;
import com.safetynet.alerts.model.PersonInfoLastNameDTO;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for PersonController.
 */
@WebMvcTest(PersonController.class)
class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person person;
    private UUID personId;

    @BeforeEach
    void setUp() {
        person = new Person();
        personId = UUID.randomUUID();
        // Reflection is used here because `id` is final and initialized in the class.
        try {
            java.lang.reflect.Field field = Person.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(person, personId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setCity("Springfield");
        person.setZip("12345");
        person.setPhone("123-456-7890");
        person.setEmail("john.doe@example.com");
    }

    /**
     * Test adding a new person successfully.
     */
    @Test
    void testAddPerson_Success() throws Exception {
        // Aucun comportement spécifique attendu, car le service ajoute simplement la personne
        Mockito.doNothing().when(personService).addPerson(any(Person.class));

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Person added successfully."));

        Mockito.verify(personService, Mockito.times(1)).addPerson(any(Person.class));
    }

    /**
     * Test ajouter une personne avec une erreur lors de la persistance.
     */
    @Test
    void testAddPerson_Error() throws Exception {
        // Simuler une exception lors de l'ajout
        Mockito.doThrow(new IOException("Simulated IO Exception"))
            .when(personService).addPerson(any(Person.class));

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while adding the person."));

        Mockito.verify(personService, Mockito.times(1)).addPerson(any(Person.class));
    }

    /**
     * Test mettre à jour une personne avec succès.
     */
    @Test
    void testUpdatePerson_Success() throws Exception {
        // Simuler une mise à jour réussie
        Mockito.when(personService.updatePerson(any(Person.class))).thenReturn(true);

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isOk())
            .andExpect(content().string("Person updated successfully."));

        Mockito.verify(personService, Mockito.times(1)).updatePerson(any(Person.class));
    }

    /**
     * Test mettre à jour une personne non trouvée.
     */
    @Test
    void testUpdatePerson_NotFound() throws Exception {
        // Simuler une mise à jour échouée (personne non trouvée)
        Mockito.when(personService.updatePerson(any(Person.class))).thenReturn(false);

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Person not found."));

        Mockito.verify(personService, Mockito.times(1)).updatePerson(any(Person.class));
    }

    /**
     * Test mettre à jour une personne avec une exception.
     */
    @Test
    void testUpdatePerson_Error() throws Exception {
        // Simuler une exception lors de la mise à jour
        Mockito.when(personService.updatePerson(any(Person.class)))
            .thenThrow(new RuntimeException("Simulated Exception"));

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while updating the person."));

        Mockito.verify(personService, Mockito.times(1)).updatePerson(any(Person.class));
    }

    /**
     * Test supprimer une personne avec succès.
     */
    @Test
    void testDeletePerson_Success() throws Exception {
        // Simuler une suppression réussie
        Mockito.when(personService.deletePerson(personId)).thenReturn(true);

        mockMvc.perform(delete("/person")
                .param("personId", personId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("Person deleted successfully."));

        Mockito.verify(personService, Mockito.times(1)).deletePerson(personId);
    }

    /**
     * Test supprimer une personne non trouvée.
     */
    @Test
    void testDeletePerson_NotFound() throws Exception {
        // Simuler une suppression échouée (personne non trouvée)
        Mockito.when(personService.deletePerson(personId)).thenReturn(false);

        mockMvc.perform(delete("/person")
                .param("personId", personId.toString()))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Person not found."));

        Mockito.verify(personService, Mockito.times(1)).deletePerson(personId);
    }

    /**
     * Test supprimer une personne avec une exception.
     */
    @Test
    void testDeletePerson_Error() throws Exception {
        // Simuler une exception lors de la suppression
        Mockito.when(personService.deletePerson(personId))
            .thenThrow(new IOException("Simulated IO Exception"));

        mockMvc.perform(delete("/person")
                .param("personId", personId.toString()))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while deleting the person."));

        Mockito.verify(personService, Mockito.times(1)).deletePerson(personId);
    }

    /**
     * Test récupérer des alertes enfants à une adresse donnée avec succès.
     */
    @Test
    void testGetChildAlertByAddress_Success() throws Exception {
        String address = "123 Main St";

        // Créer des enfants et membres du foyer
        ChildAlertDTO childAlert = new ChildAlertDTO();
        childAlert.setFirstName("Jane");
        childAlert.setLastName("Doe");
        childAlert.setAge(10);
        Set<PersonInfoDTO> householdMembers = Set.of(
            new PersonInfoDTO("John", "Doe", "123 Main St", "123-456-7890")
        );
        childAlert.setHouseholdMembers(householdMembers);
        Set<ChildAlertDTO> children = Set.of(childAlert);

        Mockito.when(personService.getChildrenByAddress(address)).thenReturn(children);

        String expectedResponse = childAlert.toString();

        mockMvc.perform(get("/childAlert")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    /**
     * Test récupérer des alertes enfants à une adresse donnée sans enfants.
     */
    @Test
    void testGetChildAlertByAddress_NoChildren() throws Exception {
        String address = "123 Main St";

        // Simuler aucun enfant trouvé
        Mockito.when(personService.getChildrenByAddress(address)).thenReturn(Set.of());

        mockMvc.perform(get("/childAlert")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(content().string("No children found at the specified address."));
    }

    /**
     * Test récupérer des alertes enfants à une adresse donnée avec une exception.
     */
    @Test
    void testGetChildAlertByAddress_Error() throws Exception {
        String address = "123 Main St";

        // Simuler une exception lors de la récupération
        Mockito.when(personService.getChildrenByAddress(address))
            .thenThrow(new RuntimeException("Simulated Exception"));

        mockMvc.perform(get("/childAlert")
                .param("address", address))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while processing the request."));
    }

    /**
     * Test récupérer des numéros de téléphone par numéro de caserne avec succès.
     */
    @Test
    void testGetPhoneAlertByFirestation_Success() throws Exception {
        int firestationNumber = 1;
        Set<String> phoneNumbers = Set.of("123-456-7890", "098-765-4321");

        Mockito.when(personService.getPhoneNumbersByFirestation(firestationNumber)).thenReturn(phoneNumbers);

        String expectedResponse = String.join("\n", phoneNumbers);

        mockMvc.perform(get("/phoneAlert")
                .param("firestation", String.valueOf(firestationNumber)))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    /**
     * Test récupérer des numéros de téléphone par numéro de caserne sans résultats.
     */
    @Test
    void testGetPhoneAlertByFirestation_NoPhoneNumbers() throws Exception {
        int firestationNumber = 1;

        Mockito.when(personService.getPhoneNumbersByFirestation(firestationNumber)).thenReturn(Set.of());

        mockMvc.perform(get("/phoneAlert")
                .param("firestation", String.valueOf(firestationNumber)))
            .andExpect(status().isOk())
            .andExpect(content().string("No phone numbers found for the specified firestation."));
    }

    /**
     * Test récupérer des numéros de téléphone par numéro de caserne avec une exception.
     */
    @Test
    void testGetPhoneAlertByFirestation_Error() throws Exception {
        int firestationNumber = 1;

        Mockito.when(personService.getPhoneNumbersByFirestation(firestationNumber))
            .thenThrow(new RuntimeException("Simulated Exception"));

        mockMvc.perform(get("/phoneAlert")
                .param("firestation", String.valueOf(firestationNumber)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while processing the request."));
    }

    /**
     * Test récupérer des informations de personnes par nom de famille avec succès.
     */
    @Test
    void testGetPersonInfoByLastName_Success() throws Exception {
        String lastName = "Doe";

        // Créer des informations de personnes
        PersonInfoLastNameDTO personInfo = new PersonInfoLastNameDTO(
            "Doe",
            "123 Main St",
            30,
            "john.doe@example.com",
            List.of("med1", "med2"),
            List.of("allergy1")
        );
        List<PersonInfoLastNameDTO> personsInfo = List.of(personInfo);

        Mockito.when(personService.getPersonsInfoByLastName(lastName)).thenReturn(personsInfo);

        String expectedResponse = personInfo.toString();

        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    /**
     * Test récupérer des informations de personnes par nom de famille sans résultats.
     */
    @Test
    void testGetPersonInfoByLastName_NoPersons() throws Exception {
        String lastName = "Doe";

        Mockito.when(personService.getPersonsInfoByLastName(lastName)).thenReturn(List.of());

        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName))
            .andExpect(status().isOk())
            .andExpect(content().string("No persons found with the last name Doe"));
    }

    /**
     * Test récupérer des informations de personnes par nom de famille avec une exception.
     */
    @Test
    void testGetPersonInfoByLastName_Error() throws Exception {
        String lastName = "Doe";

        Mockito.when(personService.getPersonsInfoByLastName(lastName))
            .thenThrow(new RuntimeException("Simulated Exception"));

        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while processing the request."));
    }

    /**
     * Test récupérer des emails communautaires par ville avec succès.
     */
    @Test
    void testGetCommunityEmailsByCity_Success() throws Exception {
        String city = "Springfield";
        Set<String> emails = Set.of("john.doe@example.com", "jane.doe@example.com");

        Mockito.when(personService.getEmailsByCity(city)).thenReturn(emails);

        String expectedResponse = String.join("\n", emails);

        mockMvc.perform(get("/communityEmail")
                .param("city", city))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    /**
     * Test récupérer des emails communautaires par ville sans résultats.
     */
    @Test
    void testGetCommunityEmailsByCity_NoEmails() throws Exception {
        String city = "Springfield";

        Mockito.when(personService.getEmailsByCity(city)).thenReturn(Set.of());

        mockMvc.perform(get("/communityEmail")
                .param("city", city))
            .andExpect(status().isOk())
            .andExpect(content().string("No email addresses found for the specified city."));
    }

    /**
     * Test récupérer des emails communautaires par ville avec une exception.
     */
    @Test
    void testGetCommunityEmailsByCity_Error() throws Exception {
        String city = "Springfield";

        Mockito.when(personService.getEmailsByCity(city))
            .thenThrow(new RuntimeException("Simulated Exception"));

        mockMvc.perform(get("/communityEmail")
                .param("city", city))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while processing the request."));
    }
}
