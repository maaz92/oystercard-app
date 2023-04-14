package com.citystoragesystems.entity;

import java.util.ArrayList;
import java.util.List;

public class Station {

    public enum Type {
        TUBE,
        BUS
    }
    private long id;

    private String name;

    private Type type;

    private List<Long> zones;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getZones() {
        return zones;
    }

    public void setZones(List<Long> zones) {
        this.zones = zones;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Station copy() {
        Station station = new Station();
        station.setId(this.getId());
        station.setName(this.getName());
        List<Long> stationsZones = new ArrayList<>(zones);
        station.setZones(stationsZones);
        return station;
    }
}
