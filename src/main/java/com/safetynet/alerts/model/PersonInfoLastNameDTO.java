package com.safetynet.alerts.model;

import java.util.List;

public class PersonInfoLastNameDTO {

    private String lastName;
    private String address;
    private final int age;
    private final String email;
    private final List<String> medications;
    private final List<String> allergies;

    public PersonInfoLastNameDTO(String lastName, String address, int age, String email, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "\nPersonInfoLastNameDTO { " +
               "lastName: '" + lastName + '\'' +
               ", address: '" + address + '\'' +
               ", age: " + age +
               ", email: '" + email + '\'' +
               ", medications: [" + String.join(", ", medications) + "]" +
               ", allergies: [" + String.join(", ", allergies) + "]" +
               " }";
    }
}
