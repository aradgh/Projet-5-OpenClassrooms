package com.safetynet.alerts.model;

import java.util.Set;
import java.util.stream.Collectors;

public class FloodStationDTO {

    private String address;
    private final Set<ResidentInfoDTO> residents;

    public FloodStationDTO() {
        this.residents = Set.of(); // Collection vide par défaut
    }


    public FloodStationDTO(String address, Set<ResidentInfoDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        String residentsString = (residents != null)
            ? residents.stream()
            .map(ResidentInfoDTO::toString)
            .collect(Collectors.joining(", "))
            : "No residents";

        return "FloodStationDTO { " +
            "address: '" + address + '\'' +
            ", residents: [" + residentsString + "\n]" +
            " }";
    }
}
