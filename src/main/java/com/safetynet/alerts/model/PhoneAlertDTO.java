package com.safetynet.alerts.model;

import java.util.List;

public class PhoneAlertDTO {

    private List<String> phoneNumbers;

    public PhoneAlertDTO(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return "PhoneAlertDTO {" +
               "phoneNumbers=[" + String.join(", ", phoneNumbers) +
               "]}";
    }
}
