package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public void addPerson(Person person) throws IOException {
        personRepository.addPerson(person);
    }

    public boolean updatePerson(Person person) throws IOException {
        Person existingPerson = personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        if (existingPerson != null) {
            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setZip(person.getZip());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());
            personRepository.updatePerson(person);
            return true;
        }
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) throws IOException {
        Person existingPerson = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (existingPerson != null) {
            personRepository.deletePerson(existingPerson.getFirstName(), existingPerson.getLastName());
            return true;
        }
        return false;
    }
}
