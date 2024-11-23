package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.ChildAlertDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonInfoLastNameDTO;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing Person-related endpoints.
 * Handles operations such as adding, updating, deleting, and retrieving person data.
 */
@RestController
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Adds a new person.
     *
     * @param person The person to add.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@RequestBody Person person) throws IOException {
        logger.info("Received request to add a person: {}", person);
        try {
            personService.addPerson(person);
            logger.info("Person added successfully: {}", person);
            return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully.");
        } catch (Exception e) {
            logger.error("Error while adding person: {}", person, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the person.");
        }
    }

    /**
     * Updates an existing person.
     *
     * @param person The person to update.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @PutMapping("/person")
    public ResponseEntity<String> updatePerson(@RequestBody Person person) throws IOException {
        logger.info("Received request to update person: {}", person);
        try {
            boolean updated = personService.updatePerson(person);
            if (updated) {
                logger.info("Person updated successfully: {}", person);
                return ResponseEntity.ok("Person updated successfully.");
            } else {
                logger.error("Person not found for update: {}", person.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
            }
        } catch (Exception e) {
            logger.error("Error while updating person: {}", person, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the person.");
        }
    }

    /**
     * Deletes a person by their ID.
     *
     * @param personId The ID of the person to delete.
     * @return A response indicating the outcome of the operation.
     * @throws IOException if an error occurs during data persistence.
     */
    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam UUID personId) throws IOException {
        logger.info("Received request to delete person with ID: {}", personId);
        try {
            boolean deleted = personService.deletePerson(personId);
            if (deleted) {
                logger.info("Person deleted successfully with ID: {}", personId);
                return ResponseEntity.ok("Person deleted successfully.");
            } else {
                logger.error("Person not found for deletion with ID: {}", personId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
            }
        } catch (Exception e) {
            logger.error("Error while deleting person with ID: {}", personId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the person.");
        }
    }

    /**
     * Retrieves children at a specific address.
     *
     * @param address The address to search.
     * @return A response containing children data or an empty message if none found.
     */
    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildAlertByAddress(@RequestParam String address) {
        logger.info("Received request for child alert at address: {}", address);
        Set<ChildAlertDTO> children = personService.getChildrenByAddress(address);

        if (children.isEmpty()) {
            logger.info("No children found at address: {}", address);
            return ResponseEntity.status(HttpStatus.OK).body("No children found at the specified address.");
        }

        String result = children.stream()
            .map(ChildAlertDTO::toString)
            .collect(Collectors.joining("\n"));

        logger.info("Child alert data retrieved for address {}: {}", address, result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Retrieves phone numbers for residents covered by a firestation.
     *
     * @param firestation The firestation number.
     * @return A response containing phone numbers or an empty message if none found.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneAlertByFirestation(@RequestParam int firestation) {
        logger.info("Received request for phone alert for firestation: {}", firestation);
        Set<String> phoneNumbers = personService.getPhoneNumbersByFirestation(firestation);

        if (phoneNumbers.isEmpty()) {
            logger.info("No phone numbers found for firestation: {}", firestation);
            return ResponseEntity.status(HttpStatus.OK).body("No phone numbers found for the specified firestation.");
        }

        String result = String.join("\n", phoneNumbers);
        logger.info("Phone alert data retrieved for firestation {}: {}", firestation, result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Retrieves information about persons with the given last name.
     *
     * @param lastName The last name to search.
     * @return A response containing person information or an empty message if none found.
     */
    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfoByLastName(@RequestParam String lastName) {
        logger.info("Received request for person info with lastName: {}", lastName);
        List<PersonInfoLastNameDTO> personsInfo = personService.getPersonsInfoByLastName(lastName);

        if (personsInfo.isEmpty()) {
            logger.info("No persons found with last name: {}", lastName);
            return ResponseEntity.status(HttpStatus.OK).body("No persons found with the last name " + lastName);
        }

        String result = personsInfo.stream()
            .map(PersonInfoLastNameDTO::toString)
            .collect(Collectors.joining("\n"));

        logger.info("Person info data retrieved for lastName {}: {}", lastName, result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Retrieves email addresses of all residents in a city.
     *
     * @param city The city to search.
     * @return A response containing email addresses or an empty message if none found.
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<String> getCommunityEmailsByCity(@RequestParam String city) {
        logger.info("Received request for community emails in city: {}", city);
        Set<String> emails = personService.getEmailsByCity(city);

        if (emails.isEmpty()) {
            logger.info("No email addresses found for city: {}", city);
            return ResponseEntity.status(HttpStatus.OK).body("No email addresses found for the specified city.");
        }

        String result = String.join("\n", emails);
        logger.info("Community email data retrieved for city {}: {}", city, result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
