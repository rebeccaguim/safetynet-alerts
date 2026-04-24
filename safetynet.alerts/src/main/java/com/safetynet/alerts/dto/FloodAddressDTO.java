package com.safetynet.alerts.dto;

import java.util.List;

/**
 * Data Transfer Object used to represent a household at a specific address during a flood.
 */
public class FloodAddressDTO {

    // Address of the household
    private String address;

    // List of residents living at this address
    private List<FloodPersonDTO> residents;

    // Constructor to initialize all fields
    public FloodAddressDTO(String address, List<FloodPersonDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    /**
     * Gets the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the list of residents
     */
    public List<FloodPersonDTO> getResidents() {
        return residents; 
    }
}