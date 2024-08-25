package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

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

}
