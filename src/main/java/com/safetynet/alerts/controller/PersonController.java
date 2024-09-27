package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) throws IOException {
        personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully.");
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) throws IOException {
        boolean updated = personService.updatePerson(person);
        if (updated) {
            return ResponseEntity.ok("Person updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam UUID personId) throws IOException {
        boolean deleted = personService.deletePerson(personId);
        if (deleted) {
            return ResponseEntity.ok("Person deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }
}
