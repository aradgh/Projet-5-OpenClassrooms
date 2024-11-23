package com.safetynet.alerts.service;

import com.safetynet.alerts.model.*;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing Firestation-related operations.
 */
@Service
public class FirestationService {

    private static final Logger logger = LogManager.getLogger(FirestationService.class);

    private final FirestationRepository firestationRepository;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;

    public FirestationService(FirestationRepository firestationRepository, PersonService personService,
                              PersonRepository personRepository,
                              MedicalRecordService medicalRecordService) {
        this.firestationRepository = firestationRepository;
        this.personService = personService;
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Adds a new firestation.
     *
     * @param firestation The firestation to add.
     * @throws IOException if an error occurs during data persistence.
     */
    public void addFirestation(Firestation firestation) throws IOException {
        logger.info("Adding firestation: {}", firestation);
        firestationRepository.addFirestation(firestation);
        logger.info("Firestation added successfully: {}", firestation);
    }

    /**
     * Updates an existing firestation.
     *
     * @param firestation The firestation to update.
     * @return True if the firestation was updated, false otherwise.
     */
    public boolean updateFirestation(Firestation firestation) {
        logger.info("Updating firestation with ID: {}", firestation.getId());
        Optional<Firestation> existingFirestationOpt = Optional.ofNullable(firestationRepository.findById(firestation.getId()));

        return existingFirestationOpt.map(existingFirestation -> {
            boolean isUpdated = updateStationIfNecessary(existingFirestation, firestation.getStation());
            isUpdated |= updateAddressIfNecessary(existingFirestation, firestation.getAddress());

            if (isUpdated) {
                try {
                    firestationRepository.updateFirestation(existingFirestation);
                    logger.info("Firestation updated successfully: {}", existingFirestation);
                } catch (IOException e) {
                    logger.error("Error while updating firestation: {}", firestation.getId(), e);
                    throw new RuntimeException(e);
                }
            }

            return isUpdated;
        }).orElseGet(() -> {
            logger.error("Firestation not found for update: {}", firestation.getId());
            return false;
        });
    }

    private boolean updateStationIfNecessary(Firestation existingFirestation, int newStation) {
        if (newStation > 0 && existingFirestation.getStation() != newStation) {
            logger.debug("Updating station number from {} to {}", existingFirestation.getStation(), newStation);
            existingFirestation.setStation(newStation);
            return true;
        }
        return false;
    }

    private boolean updateAddressIfNecessary(Firestation existingFirestation, String newAddress) {
        if (newAddress != null && !newAddress.isEmpty() && !existingFirestation.getAddress().equals(newAddress)) {
            logger.debug("Updating address from '{}' to '{}'", existingFirestation.getAddress(), newAddress);
            existingFirestation.setAddress(newAddress);
            return true;
        }
        return false;
    }

    /**
     * Deletes a firestation.
     *
     * @param firestationId The ID of the firestation to delete.
     * @return True if the firestation was deleted, false otherwise.
     * @throws IOException if an error occurs during data persistence.
     */
    public boolean deleteFirestation(UUID firestationId) throws IOException {
        logger.info("Deleting firestation with ID: {}", firestationId);
        boolean deleted = firestationRepository.deleteFirestation(firestationId);
        if (deleted) {
            logger.info("Firestation deleted successfully: {}", firestationId);
        } else {
            logger.error("Firestation not found for deletion: {}", firestationId);
        }
        return deleted;
    }

    /**
     * Retrieves the coverage information for a firestation.
     *
     * @param stationNumber The firestation number.
     * @return A FirestationCoverageDTO containing the coverage information.
     */
    public FirestationCoverageDTO getCoverageByStation(int stationNumber) {
        logger.info("Retrieving coverage for firestation number: {}", stationNumber);

        List<Person> personsCovered = personService.getPersonsByFirestation(stationNumber);
        Set<PersonInfoDTO> personInfoList = mapToPersonInfoDTOSet(personsCovered);
        int numberOfChildren = countChildren(personsCovered);
        int numberOfAdults = personsCovered.size() - numberOfChildren;

        FirestationCoverageDTO responseDTO = new FirestationCoverageDTO();
        responseDTO.setPersons(personInfoList);
        responseDTO.setNumberOfChildren(numberOfChildren);
        responseDTO.setNumberOfAdults(numberOfAdults);

        logger.info("Coverage retrieved for firestation number {}: {}", stationNumber, responseDTO);
        return responseDTO;
    }

    /**
     * Maps a list of persons to a set of PersonInfoDTO objects.
     *
     * @param personsCovered The list of persons to map.
     * @return A set of PersonInfoDTO objects.
     */
    private Set<PersonInfoDTO> mapToPersonInfoDTOSet(List<Person> personsCovered) {
        logger.debug("Mapping {} persons to PersonInfoDTO", personsCovered.size());

        Set<PersonInfoDTO> personInfoDTOSet = personsCovered.stream()
            .map(person -> {
                PersonInfoDTO infoDTO = new PersonInfoDTO();
                infoDTO.setFirstName(person.getFirstName());
                infoDTO.setLastName(person.getLastName());
                infoDTO.setAddress(person.getAddress());
                infoDTO.setPhone(person.getPhone());
                return infoDTO;
            })
            .collect(Collectors.toSet());

        logger.debug("Successfully mapped {} persons to PersonInfoDTO.", personInfoDTOSet.size());
        return personInfoDTOSet;
    }

    /**
     * Counts the number of children (age <= 18) in a list of persons.
     *
     * @param persons The list of persons to filter.
     * @return The count of children.
     */
    private int countChildren(List<Person> persons) {
        logger.debug("Counting children from a list of {} persons", persons.size());

        int numberOfChildren = (int) persons.stream()
            .filter(medicalRecordService::isChild)
            .count();

        logger.debug("Number of children counted: {}", numberOfChildren);
        return numberOfChildren;
    }

    /**
     * Retrieves fire alert information for a specific address.
     *
     * @param address The address to query.
     * @return A FireAlertDTO containing the residents and firestation information.
     */
    public FireAlertDTO getResidentsByAddress(String address) {
        logger.info("Retrieving residents by address: {}", address);

        List<Person> residents = personRepository.findByAddress(address);
        Optional<Firestation> firestation = firestationRepository.findByAddress(address);
        int firestationNumber = firestation.map(Firestation::getStation).orElse(0);

        Set<ResidentInfoDTO> residentInfoList = residents.stream()
            .map(this::createResidentInfoDTO)
            .collect(Collectors.toSet());

        FireAlertDTO fireAlertDTO = new FireAlertDTO(firestationNumber, residentInfoList);
        logger.info("Residents retrieved for address {}: {}", address, fireAlertDTO);

        return fireAlertDTO;
    }

    /**
     * Retrieves households covered by the specified firestations.
     *
     * @param stationNumbers The set of station numbers.
     * @return A list of FloodStationDTO objects containing households and their residents.
     */
    public List<FloodStationDTO> getHouseholdsByStations(Set<Integer> stationNumbers) {
        logger.info("Fetching households for firestation numbers: {}", stationNumbers);

        List<Firestation> firestations = firestationRepository.findByStations(stationNumbers);
        logger.debug("Found {} firestations for the given station numbers.", firestations.size());

        Set<String> addresses = firestations.stream()
            .map(Firestation::getAddress)
            .collect(Collectors.toSet());
        logger.debug("Addresses extracted from firestations: {}", addresses);

        Map<String, List<Person>> personsByAddress = personRepository.findByAddresses(addresses).stream()
            .collect(Collectors.groupingBy(Person::getAddress));
        logger.debug("Grouped {} persons by address.", personsByAddress.size());

        List<FloodStationDTO> households = personsByAddress.entrySet().stream()
            .map(entry -> {
                String address = entry.getKey();
                Set<ResidentInfoDTO> residentInfoList = entry.getValue().stream()
                    .map(this::createResidentInfoDTO)
                    .collect(Collectors.toSet());

                return new FloodStationDTO(address, residentInfoList);
            })
            .toList();

        logger.info("Successfully retrieved {} households for the specified stations.", households.size());
        return households;
    }

    /**
     * Creates a ResidentInfoDTO from a Person object, including medical information.
     *
     * @param person The person to transform.
     * @return A ResidentInfoDTO containing the person's details and medical information.
     */
    ResidentInfoDTO createResidentInfoDTO(Person person) {
        logger.debug("Creating ResidentInfoDTO for person: {}", person);

        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByPerson(person.getFirstName(), person.getLastName());
        int age = medicalRecord != null ? medicalRecordService.calculateAge(medicalRecord.getBirthdate()) : 0;
        List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
        List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

        ResidentInfoDTO residentInfoDTO = new ResidentInfoDTO(person.getLastName(), person.getPhone(), age, medications, allergies);
        logger.debug("Created ResidentInfoDTO: {}", residentInfoDTO);

        return residentInfoDTO;
    }
}
