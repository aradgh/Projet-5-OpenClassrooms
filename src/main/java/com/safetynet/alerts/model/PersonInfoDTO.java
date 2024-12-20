package com.safetynet.alerts.model;

public class PersonInfoDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonInfoDTO() {}

    public PersonInfoDTO(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "\n\tPersonInfoDTO { " +
               "firstName: '" + firstName + '\'' +
               ", lastName: '" + lastName + '\'' +
               ", address: '" + address + '\'' +
               ", phone: '" + phone + '\'' +
               " }";
    }
}
