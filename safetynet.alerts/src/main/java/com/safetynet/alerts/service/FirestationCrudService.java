package com.safetynet.alerts.service;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.DataLoader;

@Service
public class FirestationCrudService {

    private final DataLoader dataLoader;

    public FirestationCrudService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public Firestation addFirestation(Firestation firestation) {
        dataLoader.getData().getFirestations().add(firestation);
        dataLoader.saveData();
        return firestation;
    }

    public Firestation updateFirestation(Firestation updatedFirestation) {
        for (Firestation firestation : dataLoader.getData().getFirestations()) {
            if (firestation.getAddress().equalsIgnoreCase(updatedFirestation.getAddress())) {

                firestation.setStation(updatedFirestation.getStation());
                dataLoader.saveData();
                return firestation;
            }
        }
        return null;
    }

    public boolean deleteByAddress(String address) {
        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getAddress().equalsIgnoreCase(address));

        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }

    public boolean deleteByStation(String station) {
        boolean removed = dataLoader.getData().getFirestations().removeIf(f ->
                f.getStation().equals(station));

        if (removed) {
            dataLoader.saveData();
        }

        return removed;
    }
}