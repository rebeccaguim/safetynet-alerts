package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.DataLoader;

/**
 * This service handles CRUD operations for firestations.
 */
@Service
public class FirestationCrudService {

    // DataLoader is used to access and save application data
    private final DataLoader dataLoader;

    // Constructor to inject the DataLoader
    public FirestationCrudService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Adds a new firestation mapping
     *
     * @param firestation the firestation to add
     * @return the added firestation
     */
    public Firestation addFirestation(Firestation firestation) {

        dataLoader.getData().getFirestations().add(firestation);
        dataLoader.saveData();

        return firestation;
    }

    /**
     * Updates an existing firestation based on its address
     *
     * @param updatedFirestation the updated firestation data
     * @return the updated firestation
     */
    public Firestation updateFirestation(Firestation updatedFirestation) {

        for (Firestation firestation : dataLoader.getData().getFirestations()) {

            if (firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress())) {

                firestation.setStation(updatedFirestation.getStation());
                dataLoader.saveData();

                return firestation;
            }
        }

        //  Replace null with exception
        throw new ResourceNotFoundException(
                "Firestation not found for address: " + updatedFirestation.getAddress()
        );
    }

    /**
     * Deletes a firestation by address
     *
     * @param address the address to delete
     * @return true if deleted
     */
    public boolean deleteByAddress(String address) {

        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getAddress().equalsIgnoreCase(address));

        if (removed) {
            dataLoader.saveData();
            return true;
        }

        //  Replace false with exception
        throw new ResourceNotFoundException(
                "Firestation not found for address: " + address
        );
    }

    /**
     * Deletes firestations by station number
     *
     * @param station the station number
     * @return true if deleted
     */
    public boolean deleteByStation(String station) {

        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getStation().equals(station));

        if (removed) {
            dataLoader.saveData();
            return true;
        }

        //  Replace false with exception
        throw new ResourceNotFoundException(
                "Firestation not found for station: " + station
        );
    }
}