package com.oystercard.repository;

import com.oystercard.entity.OysterCard;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OysterCardRepository {

    private Map<Long, OysterCard> oysterCards;

    public OysterCardRepository() {
        this.oysterCards = new HashMap<>();
    }

    public OysterCard create() {
        OysterCard oysterCard = new OysterCard();
        long generatedId = 1+oysterCards.size();
        oysterCard.setId(generatedId);
        oysterCards.put(generatedId, oysterCard);
        return oysterCard.copy();
    }

    public Optional<OysterCard> get(long id) {
        OysterCard oysterCard = oysterCards.get(id);
        if(oysterCard==null) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(oysterCard.copy());
    }

    public OysterCard addBalance(long id, double amount) {
        OysterCard oysterCard = oysterCards.get(id);
        if(oysterCard == null) {
            throw new RuntimeException("Card with id " + id +" not found");
        }
        oysterCard.setBalance(oysterCard.getBalance()+amount);
        return oysterCard.copy();
    }

    public OysterCard reserveBalance(long id, double amount) {
        OysterCard oysterCard = oysterCards.get(id);
        if(oysterCard == null) {
            throw new RuntimeException("Card with id " + id +" not found");
        }
        oysterCard.setBalance(oysterCard.getBalance()-amount);
        oysterCard.setReservedBalance(amount);
        return oysterCard.copy();
    }

    public OysterCard deductFare(long id, double fare) {
        OysterCard oysterCard = oysterCards.get(id);
        if(oysterCard == null) {
            throw new RuntimeException("Card with id " + id +" not found");
        }
        double remaining = oysterCard.getReservedBalance() - fare;
        oysterCard.setBalance(oysterCard.getBalance()+remaining);
        oysterCard.setReservedBalance(0D);
        return oysterCard.copy();
    }
}
