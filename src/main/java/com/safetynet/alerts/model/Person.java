package com.safetynet.alerts.model;

import java.util.UUID;

public class Person {
    private UUID id = UUID.randomUUID();
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public Person() {}

    public Person(
        String firstName, String lastName, String address, String city, String zip, String phone, String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Person copy() {
        Person copy = new Person(this.firstName, this.lastName, this.address, this.city, this.zip, this.phone, this.email);
        copy.setId(this.id); // Ajoutez l'ID à la copie
        return copy;
    }

    private void setId(UUID id) {
        this.id = id;
    }

}
