package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class OysterCardAddBalanceEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.ADD_BALANCE;

    private OysterCardRepository oysterCardRepository;

    private CardEventRepository cardEventRepository;

    public OysterCardAddBalanceEventProcessor(OysterCardRepository oysterCardRepository, CardEventRepository cardEventRepository) {
        this.oysterCardRepository = oysterCardRepository;
        this.cardEventRepository = cardEventRepository;
    }

    @Override
    public boolean matches(OysterCardEventType oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE == oysterCardEventType;
    }

    public void process(OysterCardEvent oysterCardEvent) {
        OysterCard oysterCard = this.oysterCardRepository.addBalance(oysterCardEvent.getCardId(), oysterCardEvent.getAmount());
        this.cardEventRepository.create(oysterCardEvent);
        System.out.println(oysterCard.toString());
    }

    @Override
    public void run(Scanner scanner) {
        System.out.println("Input the id of the card");
        String input = scanner.nextLine();
        long cardId = Long.parseLong(input);
        if(!this.oysterCardRepository.get(cardId).isPresent()) {
            System.out.println("Card with this id does not exist");
            return;
        }
        System.out.println("Input the amount to be added to the card");
        input = scanner.nextLine();
        Double amount = Double.parseDouble(input);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardEventType(OYSTER_CARD_EVENT_TYPE);
        oysterCardEvent.setCardId(cardId);
        oysterCardEvent.setAmount(amount);
        process(oysterCardEvent);
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
