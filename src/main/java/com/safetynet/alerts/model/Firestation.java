package com.safetynet.alerts.model;

import java.util.UUID;

public class Firestation {
    private final UUID id = UUID.randomUUID();
    private String address;
    private int station;

    public Firestation() {}

    public Firestation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() { return station; }

    public void setStation(int station) {
        this.station = station;
    }
}
