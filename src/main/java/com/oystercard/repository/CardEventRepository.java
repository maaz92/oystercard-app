package com.oystercard.repository;

import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardEventRepository {

    private Map<Long, List<OysterCardEvent>> oysterCardTapInTapOutEvents;

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
        List<OysterCardEvent> oysterCardEventList = this.oysterCardTapInTapOutEvents.getOrDefault(oysterCardEvent.getCardId(), new ArrayList<>());
        oysterCardEventList.add(oysterCardEvent.copy());
        oysterCardTapInTapOutEvents.put(oysterCardEvent.getCardId(), oysterCardEventList);
        return oysterCardEvent;
    }

    public Optional<OysterCardEvent> getLastTapInTapOutEvent(long cardId) {
        if(oysterCardTapInTapOutEvents.get(cardId) == null || oysterCardTapInTapOutEvents.get(cardId).isEmpty()) {
            return Optional.ofNullable(null);
        }
        List<OysterCardEvent> oysterCardEvents = oysterCardTapInTapOutEvents.get(cardId);
        OysterCardEvent oysterCardEvent = oysterCardEvents.get(oysterCardEvents.size()-1);
        return Optional.ofNullable(oysterCardEvent.copy());
    }

}
