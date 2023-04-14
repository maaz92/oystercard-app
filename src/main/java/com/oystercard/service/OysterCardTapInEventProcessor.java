package com.citystoragesystems.service;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.OysterCardEvent;
import com.citystoragesystems.entity.OysterCardEventType;
import com.citystoragesystems.entity.Station;
import com.citystoragesystems.repository.CardEventRepository;
import com.citystoragesystems.repository.OysterCardRepository;
import com.citystoragesystems.repository.StationRepository;
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

    public OysterCardTapInEventProcessor(OysterCardRepository oysterCardRepository, StationRepository stationRepository, CardEventRepository cardEventRepository) {
        this.oysterCardRepository = oysterCardRepository;
        this.stationRepository = stationRepository;
        this.cardEventRepository = cardEventRepository;
    }
    private OysterCardEventType OYSTER_CARD_EVENT_TYPE = OysterCardEventType.TAP_IN;
    @Override
    public void process(OysterCardEvent oysterCardEvent) {

    }

    @Override
    public boolean matches(String oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE.name().equals(oysterCardEventType);
    }

    public OysterCard reserveMaximumBalance(OysterCard oysterCard, Station station) {
        if(station.getType() == Station.Type.BUS) {
            // reserve maximum balance for bus
        } else if(station.getType() == Station.Type.TUBE) {
            // reserve maximum balance fo tube
        }
        return null;
    }

    private void deductReservedAmount(OysterCard oysterCard) {
        //
    }
    public OysterCard chargeTapIn(long cardId, long stationId) {
        Optional<OysterCard> oysterCard = this.oysterCardRepository.get(cardId);
        Optional<Station> station = this.stationRepository.get(stationId);
        Optional<OysterCardEvent> lastOysterCardEvent = this.cardEventRepository.getLastTapInTapOutEvent(cardId);
        if(!lastOysterCardEvent.isPresent() || OysterCardEventType.TAP_IN == lastOysterCardEvent.get().getCardEventType()) {
            deductReservedAmount(oysterCard.get());
        }
        return reserveMaximumBalance(oysterCard.get(), station.get());
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
        input = scanner.nextLine();
        long stationId = Long.parseLong(input);
        if(!this.stationRepository.get(stationId).isPresent()) {
            System.out.println("Station with this id does not exist");
            return;
        }
        OysterCard oysterCard = chargeTapIn(cardId, stationId);
        System.out.println(oysterCard.toString());
    }

    @Override
    public boolean validate(OysterCardEvent oysterCardEvent) {
        boolean validationResult = !(oysterCardEvent.getCardId() == null
                || oysterCardEvent.getStationId() == null);
        if(!validationResult) {
            LOG.error("Error validating oyster card event {}. Please check the event", oysterCardEvent);
        }
        return validationResult;
    }

    @Override
    public void printOysterCardEventType() {
        System.out.println(OYSTER_CARD_EVENT_TYPE);
    }
}
