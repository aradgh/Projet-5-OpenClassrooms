package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonRepository {
    private Map<String, Person> persons = new HashMap<>();

    // Initialisation des données
    public PersonRepository(List<Person> persons) {
        for (Person person : persons) {
            String key = person.getFirstName() + person.getLastName();
            this.persons.put(key, person);
        }
    }

    public List<Person> findAll() {
        return new ArrayList<>(persons.values());
    }

    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return persons.get(firstName + lastName);
    }

    public void addPerson(Person person) throws IOException {
        String key = person.getFirstName() + person.getLastName();
        persons.put(key, person);
        saveToFile();
    }

    public void updatePerson(Person person) throws IOException {
        String key = person.getFirstName() + person.getLastName();
        if (persons.containsKey(key)) {
            persons.put(key, person);
            saveToFile();
        }
    }

    public void deletePerson(String firstName, String lastName) throws IOException {
        String key = firstName + lastName;
        if (persons.containsKey(key)) {
            persons.remove(key);
            saveToFile();
        }
    }
// Utiliser l'overloading/polymorphing
    private void saveToFile() throws IOException {
        JsonFileUtil.saveData();
    }
}
