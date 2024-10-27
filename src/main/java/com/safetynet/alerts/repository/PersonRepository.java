package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.safetynet.alerts.repository.Data.persons;
import static com.safetynet.alerts.repository.JsonFileUtil.saveData;

@Repository
public class PersonRepository {
    public List<Person> findAll() {
        return persons;
    }

    public Person findById(UUID personId) {
        return persons.stream()
            .filter(person -> person.getId().equals(personId))
            .findFirst()
            .orElse(null);
    }

    public void addPerson(Person person) throws IOException {
        persons.add(person);
        saveData();
    }

    public void updatePerson(Person person) throws IOException {
        if (this.findById(person.getId()) != null) {
            persons.set(persons.indexOf(this.findById(person.getId())), person);
            saveData();
        }
    }

    public boolean deletePerson(UUID personId) throws IOException {
        if (this.findById(personId) != null) {
            persons.remove(this.findById(personId));
            saveData();
            return true;
        }
        return false;
    }

    public List<Person> findByAddress(String address) {
        return persons.stream()
            .filter(person -> person.getAddress().contains(address))
            .toList();
    }

    public List<Person> findByCity(String city) {
        return persons.stream()
            .filter(person -> person.getCity().equals(city))
            .toList();
    }
}
