package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent a fire response.
 */
public class FireResponseDTO {

    // Firestation number covering the address
    private int stationNumber;

    // List of residents living at the address
    private List<FirePersonDTO> residents;

    // Constructor to initialize all fields
    public FireResponseDTO(int stationNumber, List<FirePersonDTO> residents) {
        this.stationNumber = stationNumber;
        this.residents = residents;
    }

    /**
     * Gets the firestation number
     */
    public int getStationNumber() {
        return stationNumber;
    }

    /**
     * Gets the list of residents
     */
    public List<FirePersonDTO> getResidents() {
        return residents;
    }
}