package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.ChildAlertDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@RequestBody Person person) throws IOException {
        personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully.");
    }

    @PutMapping("/person")
    public ResponseEntity<String> updatePerson(@RequestBody Person person) throws IOException {
        boolean updated = personService.updatePerson(person);
        if (updated) {
            return ResponseEntity.ok("Person updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam UUID personId) throws IOException {
        boolean deleted = personService.deletePerson(personId);
        if (deleted) {
            return ResponseEntity.ok("Person deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildAlertByAddress(@RequestParam String address) {
        Set<ChildAlertDTO> children = personService.getChildrenByAddress(address);

        if (children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No children found at the specified address.");
        }

        String result = children.stream()
            .map(ChildAlertDTO::toString)
            .collect(Collectors.joining("\n"));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneAlertByFirestation(@RequestParam int firestation) {
        Set<String> phoneNumbers  = personService.getPhoneNumbersByFirestation(firestation);

        if (phoneNumbers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No phone numbers found for the specified firestation.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(phoneNumbers.toString());
    }
}
