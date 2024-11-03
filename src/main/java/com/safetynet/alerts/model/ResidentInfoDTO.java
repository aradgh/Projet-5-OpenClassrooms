package com.safetynet.alerts.model;

import java.util.List;

public class ResidentInfoDTO {

    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
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
