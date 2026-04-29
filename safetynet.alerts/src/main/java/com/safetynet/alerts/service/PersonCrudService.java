package com.safetynet.alerts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service handles CRUD operations for persons.
 */
@Service
public class PersonCrudService {

    // Logger used to track CRUD operations
    private static final Logger logger = LoggerFactory.getLogger(PersonCrudService.class);

    // DataLoader is used to access and save application data
    private final DataLoader dataLoader;

    // Constructor to inject the DataLoader
    public PersonCrudService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Adds a new person
     *
     * @param person the person to add
     * @return the added person
     */
    public Person addPerson(Person person) {

        logger.info("Adding person: {} {}", person.getFirstName(), person.getLastName());

        dataLoader.getData().getPersons().add(person);
        dataLoader.saveData();

        return person;
    }

    /**
     * Updates an existing person using first name and last name
     *
     * @param updatedPerson the updated person data
     * @return the updated person
     */
    public Person updatePerson(Person updatedPerson) {

        logger.info("Updating person: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());

        for (Person person : dataLoader.getData().getPersons()) {

            if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                dataLoader.saveData();

                logger.info("Person updated successfully: {} {}", person.getFirstName(), person.getLastName());

                return person;
            }
        }

        //  Instead of returning null → throw exception
        logger.warn("Person not found for update: {} {}", updatedPerson.getFirstName(), updatedPerson.getLastName());

        throw new ResourceNotFoundException(
                "Person not found: " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName()
        );
    }

    /**
     * Deletes a person using first name and last name
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return true if deleted
     */
    public boolean deletePerson(String firstName, String lastName) {

        logger.info("Deleting person: {} {}", firstName, lastName);

        boolean removed = dataLoader.getData().getPersons().removeIf(person ->
                person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName));

        if (removed) {
            dataLoader.saveData();
            logger.info("Person deleted successfully: {} {}", firstName, lastName);
            return true;
        }

        //  Instead of returning false → throw exception
        logger.warn("Person not found for deletion: {} {}", firstName, lastName);

        throw new ResourceNotFoundException(
                "Person not found: " + firstName + " " + lastName
        );
    }
}