package com.safetynet.alerts.dto;

import java.util.List;

public class FloodAddressDTO {

    private String address;
    private List<FloodPersonDTO> residents;

    public FloodAddressDTO(String address, List<FloodPersonDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public List<FloodPersonDTO> getResidents() {
        return residents;
    }
}