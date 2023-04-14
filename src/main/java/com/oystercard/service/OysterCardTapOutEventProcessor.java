package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.entity.Station;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import com.oystercard.repository.StationRepository;
import com.oystercard.service.fare.FareProcessingStratergy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class OysterCardTapOutEventProcessor implements OysterCardEventProcessor {

    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.TAP_OUT;

    private OysterCardRepository oysterCardRepository;

    private StationRepository stationRepository;

    private CardEventRepository cardEventRepository;

    private FareProcessingStratergy fareProccessingStratergy;

    public OysterCardTapOutEventProcessor(OysterCardRepository oysterCardRepository, StationRepository stationRepository, CardEventRepository cardEventRepository, FareProcessingStratergy fareProccessingStratergy) {
        this.oysterCardRepository = oysterCardRepository;
        this.stationRepository = stationRepository;
        this.cardEventRepository = cardEventRepository;
        this.fareProccessingStratergy = fareProccessingStratergy;
    }

    @Override
    public boolean matches(OysterCardEventType oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE == oysterCardEventType;
    }

    public OysterCard chargeTapOut(long cardId, long stationId) {
        Optional<OysterCard> oysterCard = this.oysterCardRepository.get(cardId);
        Optional<Station> station = this.stationRepository.get(stationId);
        Optional<OysterCardEvent> lastOysterCardEvent = this.cardEventRepository.getLastTapInTapOutEvent(cardId);
        if(!lastOysterCardEvent.isPresent() || OysterCardEventType.TAP_IN != lastOysterCardEvent.get().getCardEventType()) {
            System.out.println("Error! The user had not tapped in his card");
            throw new RuntimeException("Error! The user had not tapped in his card");
        }
        Optional<Station> sourceStation = this.stationRepository.get(lastOysterCardEvent.get().getStationId());
        double fare = fareProccessingStratergy.calculateFare(sourceStation.get(), station.get());
        System.out.println(fare);
        return this.oysterCardRepository.deductFare(oysterCard.get().getId(), fare);
    }

    public void process(OysterCardEvent oysterCardEvent) {
        OysterCard oysterCard = chargeTapOut(oysterCardEvent.getCardId(), oysterCardEvent.getStationId());
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
        System.out.println("Input the id of the station");
        this.stationRepository.printStationInputs();
        input = scanner.nextLine();
        long stationId = Long.parseLong(input);
        if(!this.stationRepository.get(stationId).isPresent()) {
            System.out.println("Station with this id does not exist");
            return;
        }
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardEventType(OYSTER_CARD_EVENT_TYPE);
        oysterCardEvent.setCardId(cardId);
        oysterCardEvent.setStationId(stationId);
        process(oysterCardEvent);
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
