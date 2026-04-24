package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

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

        // Add firestation to the list
        dataLoader.getData().getFirestations().add(firestation);

        // Save updated data to JSON file
        dataLoader.saveData();

        // Return the added firestation
        return firestation;
    }

    /**
     * Updates an existing firestation based on its address
     *
     * @param updatedFirestation the updated firestation data
     * @return the updated firestation or null if not found
     */
    public Firestation updateFirestation(Firestation updatedFirestation) {

        // Loop through all firestations
        for (Firestation firestation : dataLoader.getData().getFirestations()) {

            // Check if the address matches
            if (firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress())) {

                // Update the station number
                firestation.setStation(updatedFirestation.getStation());

                // Save updated data
                dataLoader.saveData();

                // Return updated firestation
                return firestation;
            }
        }

        // Return null if no matching firestation is found
        return null;
    }

    /**
     * Deletes a firestation by address
     *
     * @param address the address to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteByAddress(String address) {

        // Remove firestation if address matches
        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getAddress().equalsIgnoreCase(address));

        // Save data only if something was removed
        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }

    /**
     * Deletes firestations by station number
     *
     * @param station the station number
     * @return true if deleted, false otherwise
     */
    public boolean deleteByStation(String station) {

        // Remove firestations with matching station number
        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getStation().equals(station));

        // Save data only if something was removed
        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }
}
