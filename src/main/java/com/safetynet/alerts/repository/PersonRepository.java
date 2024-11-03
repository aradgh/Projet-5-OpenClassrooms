package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
            .filter(person -> person.getAddress().equals(address))
            .toList();
    }

    public List<Person> findByCity(String city) {
        return persons.stream()
            .filter(person -> person.getCity().equals(city))
            .toList();
    }

    public Collection<Person> findByAddresses(Set<String> addresses) {
        return persons.stream()
            .filter(person -> addresses.contains(person.getAddress()))
            .toList();
    }

    public List<Person> findByLastName(String lastName) {
        return persons.stream()
            .filter(person -> person.getLastName().equals(lastName))
            .toList();
    }
}
