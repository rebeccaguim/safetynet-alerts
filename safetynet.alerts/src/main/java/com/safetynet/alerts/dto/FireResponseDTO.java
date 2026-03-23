package com.safetynet.alerts.dto;

import java.util.List;

public class FireResponseDTO {

    private int stationNumber;
    private List<FirePersonDTO> residents;

    public FireResponseDTO(int stationNumber, List<FirePersonDTO> residents) {
        this.stationNumber = stationNumber;
        this.residents = residents;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public List<FirePersonDTO> getResidents() {
        return residents;
    }
}