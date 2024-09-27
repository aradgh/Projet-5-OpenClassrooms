package com.safetynet.alerts.model;

import java.util.UUID;

public class Firestation {
    private final UUID id = UUID.randomUUID();
    private String address;
    private String station;

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
