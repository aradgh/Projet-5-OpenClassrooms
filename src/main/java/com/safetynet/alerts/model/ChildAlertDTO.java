package com.safetynet.alerts.model;

import java.util.Set;
import java.util.stream.Collectors;

public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private Set<PersonInfoDTO> householdMembers;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHouseholdMembers(Set<PersonInfoDTO> householdMembers) {
        this.householdMembers = householdMembers;
    }

    @Override
    public String toString() {
        String membersString = (householdMembers != null) ? householdMembers.stream()
            .map(PersonInfoDTO::toString)
            .collect(Collectors.joining(", ")) : "No household members";

        return "Child { " +
            "firstName: '" + firstName + '\'' +
            ", lastName: '" + lastName + '\'' +
            ", age: " + age +
            ", householdMembers: [" + membersString + "]" +
            " }";
    }

}