package com.citystoragesystems.repository;

import com.citystoragesystems.entity.OysterCardEvent;
import com.citystoragesystems.entity.OysterCardEventType;

import java.util.*;

public class CardEventRepository {

    private Map<Long, Stack<OysterCardEvent>> oysterCardTapInTapOutEvents;

    private List<OysterCardEvent> oysterCardOtherEvents;

    public CardEventRepository() {
        this.oysterCardTapInTapOutEvents = new HashMap<>();
        this.oysterCardOtherEvents = new ArrayList<>();
    }

    public OysterCardEvent create(OysterCardEvent oysterCardEvent) {
        if(oysterCardEvent.getCardEventType() != OysterCardEventType.TAP_IN && oysterCardEvent.getCardEventType() != OysterCardEventType.TAP_OUT) {
            this.oysterCardOtherEvents.add(oysterCardEvent.copy());
            return oysterCardEvent;
        }
        Stack<OysterCardEvent> oysterCardEventList = this.oysterCardTapInTapOutEvents.getOrDefault(oysterCardEvent.getCardId(), new Stack<>());
        oysterCardEventList.add(oysterCardEvent.copy());
        oysterCardTapInTapOutEvents.put(oysterCardEvent.getCardId(), oysterCardEventList);
        return oysterCardEvent;
    }

    public Optional<OysterCardEvent> getLastTapInTapOutEvent(long cardId) {
        if(oysterCardTapInTapOutEvents.get(cardId) == null || oysterCardTapInTapOutEvents.get(cardId).isEmpty()) {
            return Optional.ofNullable(null);
        }
        OysterCardEvent oysterCardEvent = oysterCardTapInTapOutEvents.get(cardId).peek();
        return Optional.ofNullable(oysterCardEvent.copy());
    }

}
