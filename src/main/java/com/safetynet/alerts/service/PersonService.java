package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

import java.util.List;

public class PersonService {
    private PersonRepository personRepository;

   public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Person findByZip(String zip) {
        return personRepository.findByZip(zip);
    }

    public Person findByAddress(String address) {
        return personRepository.findByAddress(address);
    }

    public Person findByCity(String city) {
        return personRepository.findByCity(city);
    }

    public Person findByPhone(String phone) {
        return personRepository.findByPhone(phone);
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

}
