package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.FirestationCoverageDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.PersonInfoDTO;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FirestationService {
    private final FirestationRepository firestationRepository;
    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;

    public FirestationService(FirestationRepository firestationRepository, PersonService personService,
                              MedicalRecordService medicalRecordService
    ) {
        this.firestationRepository = firestationRepository;
        this.personService = personService;
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

}
