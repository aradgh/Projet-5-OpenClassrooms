package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully.");
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        boolean updated = personService.updatePerson(person);
        if (updated) {
            return ResponseEntity.ok("Person updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
            return ResponseEntity.ok("Person deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }
}
