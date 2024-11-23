package com.safetynet.alerts.model;

import java.util.Set;
import java.util.stream.Collectors;

public class FireAlertDTO {

    private int firestationNumber = 0;
    private Set<ResidentInfoDTO> residents = Set.of();

    public FireAlertDTO() {
    }

    public FireAlertDTO(int firestationNumber, Set<ResidentInfoDTO> residents) {
        this.firestationNumber = firestationNumber;
        this.residents = residents;
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

        return "FireAlertDTO { " +
               "firestationNumber: " + firestationNumber +
               ", residents: [" + residentsString + "\n]" +
               " }";
    }
}
