package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;

    public PersonService(PersonRepository personRepository, FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }

    public void addPerson(Person person) throws IOException {
        personRepository.addPerson(person);
    }

    public boolean updatePerson(Person personToUpdate) {
        Optional<Person> existingPersonOpt = Optional.ofNullable(personRepository.findById(personToUpdate.getId()));

        return existingPersonOpt.map(existingPerson -> {
            boolean isUpdated = updateFirstNameIfNecessary(existingPerson, personToUpdate.getFirstName());
            isUpdated |= updateLastNameIfNecessary(existingPerson, personToUpdate.getLastName());
            isUpdated |= updateAddressIfNecessary(existingPerson, personToUpdate.getAddress());
            isUpdated |= updateCityIfNecessary(existingPerson, personToUpdate.getCity());
            isUpdated |= updateZipIfNecessary(existingPerson, personToUpdate.getZip());
            isUpdated |= updatePhoneIfNecessary(existingPerson, personToUpdate.getPhone());
            isUpdated |= updateEmailIfNecessary(existingPerson, personToUpdate.getEmail());

            if (isUpdated) {
                try {
                    personRepository.updatePerson(existingPerson);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return isUpdated;
        }).orElse(false);
    }

    public boolean deletePerson(UUID personId) throws IOException {
        return personRepository.deletePerson(personId);
    }

    private boolean updateFirstNameIfNecessary(Person existingPerson, String newFirstName) {
        if (newFirstName != null && !newFirstName.isEmpty() && !existingPerson.getFirstName().equals(newFirstName)) {
            existingPerson.setFirstName(newFirstName);
            return true;
        }
        return false;
    }

    private boolean updateLastNameIfNecessary(Person existingPerson, String newLastName) {
        if (newLastName != null && !newLastName.isEmpty() && !existingPerson.getLastName().equals(newLastName)) {
            existingPerson.setLastName(newLastName);
            return true;
        }
        return false;
    }

    private boolean updateAddressIfNecessary(Person existingPerson, String newAddress) {
        if (newAddress != null && !newAddress.isEmpty() && !existingPerson.getAddress().equals(newAddress)) {
            existingPerson.setAddress(newAddress);
            return true;
        }
        return false;
    }

    private boolean updateCityIfNecessary(Person existingPerson, String newCity) {
        if (newCity != null && !newCity.isEmpty() && !existingPerson.getCity().equals(newCity)) {
            existingPerson.setCity(newCity);
            return true;
        }
        return false;
    }

    private boolean updateZipIfNecessary(Person existingPerson, String newZip) {
        if (newZip != null && !newZip.isEmpty() && !existingPerson.getZip().equals(newZip)) {
            existingPerson.setZip(newZip);
            return true;
        }
        return false;
    }

    private boolean updatePhoneIfNecessary(Person existingPerson, String newPhone) {
        if (newPhone != null && !newPhone.isEmpty() && !existingPerson.getPhone().equals(newPhone)) {
            existingPerson.setPhone(newPhone);
            return true;
        }
        return false;
    }

    private boolean updateEmailIfNecessary(Person existingPerson, String newEmail) {
        if (newEmail != null && !newEmail.isEmpty() && !existingPerson.getEmail().equals(newEmail)) {
            existingPerson.setEmail(newEmail);
            return true;
        }
        return false;
    }

    public List<Person> getPersonsByAddress(String address) {
        return personRepository.findByAddress(address);
    }

    public List<Person> getPersonsByFirestation(int stationNumber) {
        List<Firestation> firestations = firestationRepository.findByStation(stationNumber);
        List<Person> persons = new ArrayList<>();
        for (Firestation firestation : firestations) {
            persons.addAll(personRepository.findByAddress(firestation.getAddress()));
        }
        return persons;
    }

    public Set<String> getEmailsByCity(String city) {
        List<Person> persons = personRepository.findByCity(city);
        return persons.stream().map(Person::getEmail).collect(Collectors.toSet());
    }

    public Set<String> getPhonesByFirestation(int stationNumber) {
        List<Person> persons = getPersonsByFirestation(stationNumber);
        return persons.stream().map(Person::getPhone).collect(Collectors.toSet());
    }

}
