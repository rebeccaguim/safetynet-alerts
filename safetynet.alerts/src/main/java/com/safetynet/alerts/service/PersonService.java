package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class PersonService {

    private final DataLoader dataLoader;

    public PersonService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public Person addPerson(Person person) {
        dataLoader.getData().getPersons().add(person);
        dataLoader.saveData();
        return person;
    }

    public Person updatePerson(Person updatedPerson) {
        for (Person person : dataLoader.getData().getPersons()) {
            if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                    && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {

                person.setAddress(updatedPerson.getAddress());
                person.setCity(updatedPerson.getCity());
                person.setZip(updatedPerson.getZip());
                person.setPhone(updatedPerson.getPhone());
                person.setEmail(updatedPerson.getEmail());

                dataLoader.saveData();
                return person;
            }
        }
        return null;
    }

    public boolean deletePerson(String firstName, String lastName) {
        boolean removed = dataLoader.getData().getPersons().removeIf(person ->
                person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName));

        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }
}