package com.oystercard.entity;

public enum Zone {
    ZONE_1(1),
    ZONE_2(2),
    ZONE_3(3);

    long id;
    Zone(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
