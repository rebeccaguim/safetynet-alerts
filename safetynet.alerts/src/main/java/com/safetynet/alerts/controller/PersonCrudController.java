package com.safetynet.alerts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonCrudService;

@RestController
@RequestMapping("/person")
public class PersonCrudController {

    private final PersonCrudService personService;

    public PersonCrudController(PersonCrudService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        return ResponseEntity.ok(personService.addPerson(person));
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        Person updated = personService.updatePerson(person);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName,
                                               @RequestParam String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Person deleted successfully");
    }
}