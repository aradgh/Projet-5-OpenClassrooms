package com.safetynet.alerts.service;

import com.safetynet.alerts.model.*;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirestationService {
    private final FirestationRepository firestationRepository;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;

    public FirestationService(FirestationRepository firestationRepository, PersonService personService,
                              PersonRepository personRepository,
                              MedicalRecordService medicalRecordService
    ) {
        this.firestationRepository = firestationRepository;
        this.personService = personService;
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
    }

    public void addFirestation(Firestation firestation) throws IOException {
        firestationRepository.addFirestation(firestation);
    }

    public boolean updateFirestation(Firestation firestation) {
        Optional<Firestation> existingFirestationOpt = Optional.ofNullable(
            firestationRepository.findById(firestation.getId()));
        return existingFirestationOpt
            .map(existingFirestation -> {
                boolean isUpdated = updateStationIfNecessary(existingFirestation, firestation.getStation());
                isUpdated |= updateAddressIfNecessary(existingFirestation, firestation.getAddress());

                if (isUpdated) {
                    try {
                        firestationRepository.updateFirestation(existingFirestation);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                return isUpdated;
            })
            .orElse(false);
    }

    private boolean updateStationIfNecessary(Firestation existingFirestation, int newStation) {
        if (newStation > 0 && existingFirestation.getStation() != newStation) {
            existingFirestation.setStation(newStation);
            return true;
        }
        return false;
    }

    private boolean updateAddressIfNecessary(Firestation existingFirestation, String newAddress) {
        if (newAddress != null && !newAddress.isEmpty() && !existingFirestation.getAddress().equals(newAddress)) {
            existingFirestation.setAddress(newAddress);
            return true;
        }
        return false;
    }

    public boolean deleteFirestation(UUID firestationId) throws IOException {
        return firestationRepository.deleteFirestation(firestationId);
    }

    public FirestationCoverageDTO getCoverageByStation(int stationNumber) {
        List<Person> personsCovered = personService.getPersonsByFirestation(stationNumber);

        Set<PersonInfoDTO> personInfoList = mapToPersonInfoDTOSet(personsCovered);
        int numberOfChildren = countChildren(personsCovered);
        int numberOfAdults = personsCovered.size() - numberOfChildren;

        FirestationCoverageDTO responseDTO = new FirestationCoverageDTO();
        responseDTO.setPersons(personInfoList);
        responseDTO.setNumberOfChildren(numberOfChildren);
        responseDTO.setNumberOfAdults(numberOfAdults);

        return responseDTO;
    }

    private Set<PersonInfoDTO> mapToPersonInfoDTOSet(List<Person> personsCovered) {
        return personsCovered.stream()
            .map(person -> {
                PersonInfoDTO infoDTO = new PersonInfoDTO();
                infoDTO.setFirstName(person.getFirstName());
                infoDTO.setLastName(person.getLastName());
                infoDTO.setAddress(person.getAddress());
                infoDTO.setPhone(person.getPhone());
                return infoDTO;
            })
            .collect(Collectors.toSet());
    }

    private int countChildren(List<Person> persons) {
        return (int) persons.stream()
            .filter(medicalRecordService::isChild)
            .count();
    }

    public FireAlertDTO getResidentsByAddress(String address) {
        List<Person> residents = personRepository.findByAddress(address);

        Optional<Firestation> firestation = firestationRepository.findByAddress(address);
        int firestationNumber = firestation.map(Firestation::getStation).orElse(0);

        Set<ResidentInfoDTO> residentInfoList = residents.stream()
            .map(this::createResidentInfoDTO)
            .collect(Collectors.toSet());

        return new FireAlertDTO(firestationNumber, residentInfoList);
    }

    public List<FloodStationDTO> getHouseholdsByStations(Set<Integer> stationNumbers) {
        List<Firestation> firestations = firestationRepository.findByStations(stationNumbers);
        Set<String> addresses = firestations.stream()
            .map(Firestation::getAddress)
            .collect(Collectors.toSet());

        Map<String, List<Person>> personsByAddress = personRepository.findByAddresses(addresses).stream()
            .collect(Collectors.groupingBy(Person::getAddress));

        return personsByAddress.entrySet().stream()
            .map(entry -> {
                String address = entry.getKey();
                Set<ResidentInfoDTO> residentInfoList = entry.getValue().stream()
                    .map(this::createResidentInfoDTO)
                    .collect(Collectors.toSet());

                return new FloodStationDTO(address, residentInfoList);
            })
            .toList();
    }

    ResidentInfoDTO createResidentInfoDTO(Person person) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByPerson(person.getFirstName(), person.getLastName());
        int age = medicalRecord != null ? medicalRecordService.calculateAge(medicalRecord.getBirthdate()) : 0;
        List<String> medications = medicalRecord != null ? medicalRecord.getMedications() : List.of();
        List<String> allergies = medicalRecord != null ? medicalRecord.getAllergies() : List.of();

        return new ResidentInfoDTO(person.getLastName(), person.getPhone(), age, medications, allergies);
    }

}
