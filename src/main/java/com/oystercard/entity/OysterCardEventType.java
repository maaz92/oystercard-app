package com.oystercard.entity;

public enum OysterCardEventType {
    CREATE(1),
    ADD_BALANCE(2),
    TAP_IN(3),
    TAP_OUT(4),
    DEACTIVATE(5);

    private int id;

    OysterCardEventType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
