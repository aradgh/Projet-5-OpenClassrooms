package com.safetynet.alerts.model;

import java.util.Set;

public class FirestationCoverageDTO {

    private Set<PersonInfoDTO> persons = Set.of();
    private int numberOfAdults;
    private int numberOfChildren;

    public void setPersons(Set<PersonInfoDTO> persons) {
        this.persons = persons;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FirestationCoverageDTO {\n");
        sb.append("  numberOfAdults: ").append(numberOfAdults).append(",\n");
        sb.append("  numberOfChildren: ").append(numberOfChildren).append(",\n");
        sb.append("  persons: [\n");

        if (persons != null) {
            for (PersonInfoDTO person : persons) {
                sb.append("    ").append(person).append(",\n");
            }
        }

        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

}
