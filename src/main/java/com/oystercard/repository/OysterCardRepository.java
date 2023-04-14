package com.citystoragesystems.repository;

import com.citystoragesystems.entity.OysterCard;
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
        long generatedId = new Random().nextLong();
        if(oysterCards.containsKey(generatedId)) {
            return create();
        }
        oysterCard.setId(generatedId);
        this.oysterCards.put(generatedId, oysterCard);
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
}
