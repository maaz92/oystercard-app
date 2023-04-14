package com.oystercard.entity;

public class OysterCard {

    private long id;

    private double balance;

    private double reservedBalance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(double reservedBalance) {
        this.reservedBalance = reservedBalance;
    }

    @Override
    public String toString() {
        return String.format("OysterCard (id=%s, balance=%s, reservedBalance=%s)", id, balance, reservedBalance);
    }

    public OysterCard copy() {
        OysterCard oysterCard = new OysterCard();
        oysterCard.setId(this.getId());
        oysterCard.setBalance(this.getBalance());
        oysterCard.setReservedBalance(this.getReservedBalance());
        return oysterCard;
    }
}
