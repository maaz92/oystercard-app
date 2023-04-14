package com.citystoragesystems.service;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.OysterCardEvent;
import com.citystoragesystems.entity.OysterCardEventType;
import com.citystoragesystems.repository.OysterCardRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class OysterCardCreateNewEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.CREATE;

    private OysterCardRepository oysterCardRepository;

    public OysterCardCreateNewEventProcessor(OysterCardRepository oysterCardRepository) {
        this.oysterCardRepository = oysterCardRepository;
    }

    @Override
    public void process(OysterCardEvent oysterCardEvent) {

    }

    @Override
    public boolean matches(String oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE.name().equals(oysterCardEventType);
    }

    @Override
    public void run(Scanner scanner) {
        OysterCard oysterCard = oysterCardRepository.create();
        System.out.println(oysterCard.toString());
    }

    @Override
    public boolean validate(OysterCardEvent oysterCardEvent) {
        return false;
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
