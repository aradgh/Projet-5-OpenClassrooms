package com.safetynet.alerts.model;

public class PersonInfoDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public String getFirstName() {
        return firstName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PersonInfoDTO { " +
               "firstName: '" + firstName + '\'' +
               ", lastName: '" + lastName + '\'' +
               ", address: '" + address + '\'' +
               ", phone: '" + phone + '\'' +
               " }";
    }
}
