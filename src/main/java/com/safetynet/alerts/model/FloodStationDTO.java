package com.safetynet.alerts.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FloodStationDTO {

    private String address;
    private Set<ResidentInfoDTO> residents;

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

    public Set<ResidentInfoDTO> getResidents() {
        return residents;
    }

    public void setResidents(Set<ResidentInfoDTO> residents) {
        this.residents = residents;
    }

    @Override
    public String toString() {
        String residentsString = residents.stream()
            .map(ResidentInfoDTO::toString)
            .collect(Collectors.joining(", "));

        return "FloodStationDTO { " +
               "address: '" + address + '\'' +
               ", residents: [" + residentsString + "\n]" +
               " }";
    }
}
