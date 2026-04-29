package com.safetynet.alerts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonCrudService;

/**
 * This controller handles CRUD operations for persons.
 */
@RestController
@RequestMapping("/person")
public class PersonCrudController {

    // Service used to manage person data
    private final PersonCrudService personService;

    // Constructor used to inject the service
    public PersonCrudController(PersonCrudService personService) {
        this.personService = personService;
    }

    /**
     * Adds a new person
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {

        // Call the service to add a new person
        return ResponseEntity.ok(personService.addPerson(person));
    }

    /**
     * Updates an existing person
     */
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {

        // Call the service to update the person
        Person updated = personService.updatePerson(person);

        // If the person is not found, return HTTP 404 (Not Found)
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        // Return the updated person
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a person using first name and last name
     */
    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName,
                                               @RequestParam String lastName) {

        // Call the service to delete the person
        boolean deleted = personService.deletePerson(firstName, lastName);

        // If the person is not found, return HTTP 404 (Not Found)
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        // Return success message
        return ResponseEntity.ok("Person deleted successfully");
    }
}