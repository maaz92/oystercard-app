package com.oystercard.service;

import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import org.springframework.stereotype.Service;

import java.util.Scanner;

//@Service
public class OysterCardDeactivateEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.DEACTIVATE;

    @Override
    public boolean matches(OysterCardEventType oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE == oysterCardEventType;
    }

    @Override
    public void run(Scanner scanner) {
        System.out.println("Not Implemented Yet");
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
