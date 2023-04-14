package com.citystoragesystems.service;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.OysterCardEvent;
import com.citystoragesystems.entity.OysterCardEventType;
import com.citystoragesystems.repository.OysterCardRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class OysterCardAddBalanceEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.ADD_BALANCE;

    private OysterCardRepository oysterCardRepository;

    public OysterCardAddBalanceEventProcessor(OysterCardRepository oysterCardRepository) {
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
        OysterCard oysterCard = this.oysterCardRepository.addBalance(cardId, amount);
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
