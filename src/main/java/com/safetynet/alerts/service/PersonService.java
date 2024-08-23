package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public boolean updatePerson(Person person) {
        Person existingPerson = personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        if (existingPerson != null) {
            personRepository.save(person);
            return true;
        }
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {
        Person existingPerson = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (existingPerson != null) {
            personRepository.delete(existingPerson);
            return true;
        }
        return false;
    }
}
