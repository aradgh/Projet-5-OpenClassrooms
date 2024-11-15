package com.safetynet.alerts.service;

import com.safetynet.alerts.model.*;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Service for managing Person-related operations.
 * Handles logic for CRUD operations and retrieving specific data about persons.
 */
@Service
public class PersonService {

    private static final Logger logger = LogManager.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordService medicalRecordService;

    public PersonService(PersonRepository personRepository, FirestationRepository firestationRepository,
                         MedicalRecordService medicalRecordService
    ) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Adds a new person to the repository.
     *
     * @param person The person to add.
     * @throws IOException if an error occurs during data persistence.
     */
    public void addPerson(Person person) throws IOException {
        logger.info("Adding a new person: {}", person);
        personRepository.addPerson(person);
        logger.info("Person added successfully.");
    }

    /**
     * Updates an existing person's details.
     *
     * @param personToUpdate The person with updated details.
     * @return True if the person was updated, false otherwise.
     */
    public boolean updatePerson(Person personToUpdate) {
        logger.info("Updating person: {}", personToUpdate);

        Optional<Person> existingPersonOpt = Optional.ofNullable(personRepository.findById(personToUpdate.getId()));

        return existingPersonOpt.map(existingPerson -> {
            boolean isUpdated = updateFields(existingPerson, personToUpdate);
            if (isUpdated) {
                try {
                    personRepository.updatePerson(existingPerson);
                    logger.info("Person updated successfully: {}", existingPerson);
                } catch (IOException e) {
                    logger.error("Error while updating person: {}", existingPerson, e);
                    throw new RuntimeException(e);
                }
            } else {
                logger.debug("No changes detected for person: {}", existingPerson);
            }
            return isUpdated;
        }).orElseGet(() -> {
            logger.error("Person not found for update: {}", personToUpdate.getId());
            return false;
        });
    }

    /**
     * Updates fields of an existing person using the values from the updated person.
     *
     * @param existingPerson The person with current data.
     * @param updatedPerson  The person with new data to update.
     * @return True if at least one field was updated, false otherwise.
     */
    private boolean updateFields(Person existingPerson, Person updatedPerson) {
        boolean isUpdated = false;

        logger.debug("Checking fields to update for person: {}", existingPerson.getId());
        isUpdated |= updateField(existingPerson::setFirstName, existingPerson.getFirstName(), updatedPerson.getFirstName(), "First Name");
        isUpdated |= updateField(existingPerson::setLastName, existingPerson.getLastName(), updatedPerson.getLastName(), "Last Name");
        isUpdated |= updateField(existingPerson::setAddress, existingPerson.getAddress(), updatedPerson.getAddress(), "Address");
        isUpdated |= updateField(existingPerson::setCity, existingPerson.getCity(), updatedPerson.getCity(), "City");
        isUpdated |= updateField(existingPerson::setZip, existingPerson.getZip(), updatedPerson.getZip(), "Zip");
        isUpdated |= updateField(existingPerson::setPhone, existingPerson.getPhone(), updatedPerson.getPhone(), "Phone");
        isUpdated |= updateField(existingPerson::setEmail, existingPerson.getEmail(), updatedPerson.getEmail(), "Email");

        logger.debug("Update status for person {}: {}", existingPerson.getId(), isUpdated);
        return isUpdated;
    }

    /**
     * Updates a specific field if the new value is different from the current value.
     *
     * @param setter      A setter method to update the field.
     * @param currentValue The current value of the field.
     * @param newValue     The new value to set.
     * @param fieldName    The name of the field being updated.
     * @param <T>          The type of the field.
     * @return True if the field was updated, false otherwise.
     */
    private <T> boolean updateField(Consumer<T> setter, T currentValue, T newValue, String fieldName) {
        if (newValue != null && !newValue.equals(currentValue)) {
            logger.debug("Updating {} from {} to {}", fieldName, currentValue, newValue);
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    /**
     * Deletes a person by their ID.
     *
     * @param personId The UUID of the person to delete.
     * @return True if the person was deleted, false otherwise.
     * @throws IOException if an error occurs during data persistence.
     */
    public boolean deletePerson(UUID personId) throws IOException {
        logger.info("Deleting person with ID: {}", personId);
        boolean deleted = personRepository.deletePerson(personId);
        if (deleted) {
            logger.info("Person deleted successfully.");
        } else {
            logger.error("Person not found for deletion with ID: {}", personId);
        }
        return deleted;
    }

    /**
     * Retrieves a list of persons covered by a firestation.
     *
     * @param stationNumber The firestation number.
     * @return A list of persons covered by the firestation.
     */
    public List<Person> getPersonsByFirestation(int stationNumber) {
        logger.info("Retrieving persons covered by firestation number: {}", stationNumber);

        List<Firestation> firestations = firestationRepository.findByStation(stationNumber);
        List<Person> persons = firestations.stream()
            .flatMap(firestation -> personRepository.findByAddress(firestation.getAddress()).stream())
            .collect(Collectors.toList());

        logger.info("Retrieved {} persons covered by firestation number: {}", persons.size(), stationNumber);
        return persons;
    }

    /**
     * Retrieves children residing at a specific address.
     *
     * @param address The address to search.
     * @return A set of ChildAlertDTO representing the children.
     */
    public Set<ChildAlertDTO> getChildrenByAddress(String address) {
        logger.info("Retrieving children by address: {}", address);

        List<Person> personsAtAddress = personRepository.findByAddress(address);
        Set<ChildAlertDTO> children = personsAtAddress.stream()
            .filter(medicalRecordService::isChild)
            .map(child -> buildChildAlertDTO(child, personsAtAddress))
            .collect(Collectors.toSet());

        logger.info("Retrieved {} children at address: {}", children.size(), address);
        return children;
    }

    /**
     * Builds a ChildAlertDTO object for a given child and their household members.
     *
     * @param child              The child to include in the DTO.
     * @param householdMembers   The list of all household members at the same address.
     * @return A ChildAlertDTO containing the child's data and household members.
     */
    private ChildAlertDTO buildChildAlertDTO(Person child, List<Person> householdMembers) {
        logger.debug("Building ChildAlertDTO for child: {}", child);

        MedicalRecord personMedicalRecord = medicalRecordService.getMedicalRecordByPerson(child.getFirstName(), child.getLastName());
        if (personMedicalRecord == null) {
            logger.error("No medical record found for child: {}", child);
            return null;
        }

        ChildAlertDTO childDTO = new ChildAlertDTO();
        childDTO.setFirstName(child.getFirstName());
        childDTO.setLastName(child.getLastName());
        childDTO.setAge(medicalRecordService.calculateAge(personMedicalRecord.getBirthdate()));
        childDTO.setHouseholdMembers(getHouseholdMembers(child, householdMembers));

        logger.debug("ChildAlertDTO created: {}", childDTO);
        return childDTO;
    }

    /**
     * Retrieves household members excluding the given child.
     *
     * @param child             The child to exclude.
     * @param personsAtAddress  The list of all persons at the address.
     * @return A set of PersonInfoDTO objects representing the household members.
     */
    private Set<PersonInfoDTO> getHouseholdMembers(Person child, List<Person> personsAtAddress) {
        logger.debug("Retrieving household members for address: {}", child.getAddress());

        Set<PersonInfoDTO> householdMembers = personsAtAddress.stream()
            .filter(member -> !member.equals(child))
            .map(this::mapToPersonInfoDTO)
            .collect(Collectors.toSet());

        logger.debug("Household members retrieved: {}", householdMembers);
        return householdMembers;
    }

    /**
     * Maps a Person object to a PersonInfoDTO.
     *
     * @param person The person to map.
     * @return A PersonInfoDTO containing the mapped data.
     */
    private PersonInfoDTO mapToPersonInfoDTO(Person person) {
        logger.debug("Mapping Person to PersonInfoDTO: {}", person);

        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName(person.getFirstName());
        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setPhone(person.getPhone());

        logger.debug("PersonInfoDTO created: {}", personInfoDTO);
        return personInfoDTO;
    }

    /**
     * Retrieves phone numbers of persons covered by a specific firestation.
     *
     * @param stationNumber The firestation number.
     * @return A set of phone numbers for the persons covered by the firestation.
     */
    public Set<String> getPhoneNumbersByFirestation(int stationNumber) {
        logger.info("Retrieving phone numbers for firestation: {}", stationNumber);

        List<Person> persons = getPersonsByFirestation(stationNumber);
        Set<String> phoneNumbers = persons.stream()
            .map(Person::getPhone)
            .collect(Collectors.toSet());

        logger.info("Retrieved {} phone numbers for firestation: {}", phoneNumbers.size(), stationNumber);
        return phoneNumbers;
    }

    /**
     * Retrieves information for persons with a given last name.
     *
     * @param lastName The last name to search for.
     * @return A list of PersonInfoLastNameDTO objects containing the person's information.
     */
    public List<PersonInfoLastNameDTO> getPersonsInfoByLastName(String lastName) {
        logger.info("Retrieving persons by last name: {}", lastName);

        List<Person> persons = personRepository.findByLastName(lastName);
        FirestationService firestationService = new FirestationService(firestationRepository, this, personRepository, medicalRecordService);

        List<PersonInfoLastNameDTO> personsInfo = persons.stream()
            .map(person -> {
                ResidentInfoDTO residentInfoDTO = firestationService.createResidentInfoDTO(person);

                return new PersonInfoLastNameDTO(
                    residentInfoDTO.getLastName(),
                    person.getAddress(),
                    residentInfoDTO.getAge(),
                    person.getEmail(),
                    residentInfoDTO.getMedications(),
                    residentInfoDTO.getAllergies()
                );
            })
            .toList();

        logger.info("Retrieved {} persons with last name: {}", personsInfo.size(), lastName);
        return personsInfo;
    }

    /**
     * Retrieves email addresses of all persons in a city.
     *
     * @param city The city to search.
     * @return A set of email addresses for all persons in the city.
     */
    public Set<String> getEmailsByCity(String city) {
        logger.info("Retrieving emails for city: {}", city);

        List<Person> persons = personRepository.findByCity(city);
        Set<String> emails = persons.stream()
            .map(Person::getEmail)
            .filter(email -> email != null && !email.isEmpty())
            .collect(Collectors.toSet());

        logger.info("Retrieved {} email addresses for city: {}", emails.size(), city);
        return emails;
    }
}
