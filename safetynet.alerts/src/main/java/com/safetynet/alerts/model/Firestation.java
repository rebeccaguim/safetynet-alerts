package com.safetynet.alerts.model;

/**
 * Represents a firestation mapping between an address and a station number.
 */
public class Firestation {

    // Address of the firestation coverage
    private String address;

    // Firestation number assigned to the address
    private String station;

    // Default constructor
    public Firestation() {}

    /**
     * Gets the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the station number
     */
    public String getStation() {
        return station;
    }

    /**
     * Sets the station number
     */
    public void setStation(String station) {
        this.station = station;
    }
}