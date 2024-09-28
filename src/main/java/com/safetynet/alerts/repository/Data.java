package com.safetynet.alerts.repository;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.util.List;

@JsonPropertyOrder({ "persons", "firestations", "medicalrecords" })
public class Data {
    public static List<Person> persons;
    public static List<Firestation> firestations;
    public static List<MedicalRecord> medicalrecords;

    public Data() {}

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}
