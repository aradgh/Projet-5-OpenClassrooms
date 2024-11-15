package com.safetynet.alerts.model;

import java.util.List;

public class ResidentInfoDTO {

    private String lastName;
    private final String phone;
    private final int age;
    private final List<String> medications;
    private final List<String> allergies;

    public ResidentInfoDTO(String lastName, String phone, int age, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    @Override
    public String toString() {
        return "\n\tResidentInfoDTO { " +
               "lastName: '" + lastName + '\'' +
               ", phone: '" + phone + '\'' +
               ", age: " + age +
               ", medications: [" + String.join(", ", medications) + "]" +
               ", allergies: [" + String.join(", ", allergies) + "]" +
               " }";
    }
}
