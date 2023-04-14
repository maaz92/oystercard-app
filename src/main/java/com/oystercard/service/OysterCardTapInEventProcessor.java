package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.entity.Station;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import com.oystercard.repository.StationRepository;
import com.oystercard.service.fare.FareProcessingStratergy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class OysterCardTapInEventProcessor implements OysterCardEventProcessor {

    private static Logger LOG = LogManager.getLogger(OysterCardTapInEventProcessor.class);

    private OysterCardRepository oysterCardRepository;

    private StationRepository stationRepository;

    private CardEventRepository cardEventRepository;

    private FareProcessingStratergy fareProccessingStratergy;

    public OysterCardTapInEventProcessor(OysterCardRepository oysterCardRepository, StationRepository stationRepository, CardEventRepository cardEventRepository, FareProcessingStratergy fareProccessingStratergy) {
        this.oysterCardRepository = oysterCardRepository;
        this.stationRepository = stationRepository;
        this.cardEventRepository = cardEventRepository;
        this.fareProccessingStratergy = fareProccessingStratergy;
    }
    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.TAP_IN;

    @Override
    public boolean matches(OysterCardEventType oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE == oysterCardEventType;
    }

    public OysterCard reserveMaximumBalance(OysterCard oysterCard, Station station) {
        double fareReserveAmount = this.fareProccessingStratergy.calculateFareReserveAmount(station);
        if(fareReserveAmount <= oysterCard.getBalance()) {
            return this.oysterCardRepository.reserveBalance(oysterCard.getId(), fareReserveAmount);
        }
        System.out.println("Insufficient Balance.");
        return oysterCard;
    }

    private OysterCard deductReservedAmount(OysterCard oysterCard) {
        return this.oysterCardRepository.deductFare(oysterCard.getId(), oysterCard.getReservedBalance());
    }
    public OysterCard chargeTapIn(long cardId, long stationId) {
        Optional<OysterCard> oysterCard = this.oysterCardRepository.get(cardId);
        Optional<Station> station = this.stationRepository.get(stationId);
        Optional<OysterCardEvent> lastOysterCardEvent = this.cardEventRepository.getLastTapInTapOutEvent(cardId);
        if(lastOysterCardEvent.isPresent() && OysterCardEventType.TAP_IN == lastOysterCardEvent.get().getCardEventType()) {
            deductReservedAmount(oysterCard.get());
        }
        return reserveMaximumBalance(oysterCard.get(), station.get());
    }

    public OysterCard process(OysterCardEvent oysterCardEvent) {
        OysterCard oysterCard = chargeTapIn(oysterCardEvent.getCardId(), oysterCardEvent.getStationId());
        this.cardEventRepository.create(oysterCardEvent);
        System.out.println(oysterCard.toString());
        return  oysterCard;
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
