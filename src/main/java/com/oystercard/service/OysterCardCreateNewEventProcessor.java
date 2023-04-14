package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class OysterCardCreateNewEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.CREATE;

    private OysterCardRepository oysterCardRepository;

    private CardEventRepository cardEventRepository;

    public OysterCardCreateNewEventProcessor(OysterCardRepository oysterCardRepository, CardEventRepository cardEventRepository) {
        this.oysterCardRepository = oysterCardRepository;
        this.cardEventRepository = cardEventRepository;
    }

    @Override
    public boolean matches(OysterCardEventType oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE == oysterCardEventType;
    }

    public OysterCard process(OysterCardEvent oysterCardEvent) {
        OysterCard oysterCard = oysterCardRepository.create();
        oysterCardEvent.setCardId(oysterCard.getId());
        this.cardEventRepository.create(oysterCardEvent);
        System.out.println(oysterCard.toString());
        return oysterCard;
    }
    @Override
    public void run(Scanner scanner) {
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardEventType(OYSTER_CARD_EVENT_TYPE);
        process(oysterCardEvent);
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
