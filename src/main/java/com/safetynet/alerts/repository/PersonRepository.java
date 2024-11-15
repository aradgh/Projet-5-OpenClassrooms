package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.safetynet.alerts.repository.Data.persons;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

/**
 * Repository for managing Person data.
 * Handles CRUD operations and specific queries for persons.
 */
@Repository
public class PersonRepository {

    private static final Logger logger = LogManager.getLogger(PersonRepository.class);

    /**
     * Finds a person by their unique ID.
     *
     * @param personId The UUID of the person.
     * @return The person if found, or null otherwise.
     */
    public Person findById(UUID personId) {
        logger.info("Searching for person with ID: {}", personId);
        Person person = persons.stream()
            .filter(p -> p.getId().equals(personId))
            .findFirst()
            .orElse(null);

        if (person != null) {
            logger.info("Person found: {}", person);
        } else {
            logger.error("No person found with ID: {}", personId);
        }
        return person;
    }

    /**
     * Adds a new person to the repository.
     *
     * @param person The person to add.
     * @throws IOException If an error occurs while saving data.
     */
    public void addPerson(Person person) throws IOException {
        logger.info("Adding new person: {}", person);
        persons.add(person);
        saveData();
        logger.info("Person added successfully.");
    }

    /**
     * Updates an existing person in the repository.
     *
     * @param person The updated person data.
     * @throws IOException If an error occurs while saving data.
     */
    public void updatePerson(Person person) throws IOException {
        logger.info("Updating person with ID: {}", person.getId());
        Person existingPerson = this.findById(person.getId());

        if (existingPerson != null) {
            persons.set(persons.indexOf(existingPerson), person);
            saveData();
            logger.info("Person updated successfully: {}", person);
        } else {
            logger.error("No person found to update with ID: {}", person.getId());
        }
    }

    /**
     * Deletes a person by their unique ID.
     *
     * @param personId The UUID of the person to delete.
     * @return True if the person was deleted, false otherwise.
     * @throws IOException If an error occurs while saving data.
     */
    public boolean deletePerson(UUID personId) throws IOException {
        logger.info("Deleting person with ID: {}", personId);
        Person personToDelete = this.findById(personId);

        if (personToDelete != null) {
            persons.remove(personToDelete);
            saveData();
            logger.info("Person deleted successfully: {}", personId);
            return true;
        } else {
            logger.error("No person found to delete with ID: {}", personId);
            return false;
        }
    }

    /**
     * Finds persons residing at a specific address.
     *
     * @param address The address to search.
     * @return A list of persons at the specified address.
     */
    public List<Person> findByAddress(String address) {
        logger.info("Finding persons by address: {}", address);
        List<Person> personsAtAddress = persons.stream()
            .filter(person -> person.getAddress().equals(address))
            .toList();
        logger.info("Found {} persons at address: {}", personsAtAddress.size(), address);
        return personsAtAddress;
    }

    /**
     * Finds persons residing in a specific city.
     *
     * @param city The city to search.
     * @return A list of persons in the specified city.
     */
    public List<Person> findByCity(String city) {
        logger.info("Finding persons by city: {}", city);
        List<Person> personsInCity = persons.stream()
            .filter(person -> person.getCity().equals(city))
            .toList();
        logger.info("Found {} persons in city: {}", personsInCity.size(), city);
        return personsInCity;
    }

    /**
     * Finds persons residing at any of the specified addresses.
     *
     * @param addresses The set of addresses to search.
     * @return A collection of persons at the specified addresses.
     */
    public Collection<Person> findByAddresses(Set<String> addresses) {
        logger.info("Finding persons by addresses: {}", addresses);
        Collection<Person> personsAtAddresses = persons.stream()
            .filter(person -> addresses.contains(person.getAddress()))
            .toList();
        logger.info("Found {} persons at specified addresses.", personsAtAddresses.size());
        return personsAtAddresses;
    }

    /**
     * Finds persons with the specified last name.
     *
     * @param lastName The last name to search.
     * @return A list of persons with the specified last name.
     */
    public List<Person> findByLastName(String lastName) {
        logger.info("Finding persons by last name: {}", lastName);
        List<Person> personsWithLastName = persons.stream()
            .filter(person -> person.getLastName().equals(lastName))
            .toList();
        logger.info("Found {} persons with last name: {}", personsWithLastName.size(), lastName);
        return personsWithLastName;
    }
}
