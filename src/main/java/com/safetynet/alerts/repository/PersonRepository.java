package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PersonRepository extends Repository<Person> {

    Person findByFirstNameAndLastName(String firstName, String lastName);

    Person findByAddress(String address);

    Person findByZip(String zip);

    Person findByCity(String city);

    Person findByPhone(String phone);

    Person findByEmail(String email);
    
    List<Person> findAll();

    Person save(Person person);
}
