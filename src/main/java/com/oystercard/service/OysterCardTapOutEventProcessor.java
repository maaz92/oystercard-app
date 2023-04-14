package com.citystoragesystems.service;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.OysterCardEvent;
import com.citystoragesystems.entity.OysterCardEventType;
import com.citystoragesystems.entity.Station;
import com.citystoragesystems.repository.CardEventRepository;
import com.citystoragesystems.repository.OysterCardRepository;
import com.citystoragesystems.repository.StationRepository;
import com.citystoragesystems.service.fare.FareProcessingStratergy;
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
    }
    @Override
    public void process(OysterCardEvent oysterCardEvent) {

    }

    @Override
    public boolean matches(String oysterCardEventType) {
        return OYSTER_CARD_EVENT_TYPE.name().equals(oysterCardEventType);
    }

    public OysterCard chargeTapOut(long cardId, long stationId) {
        Optional<OysterCard> oysterCard = this.oysterCardRepository.get(cardId);
        Optional<Station> station = this.stationRepository.get(stationId);
        Optional<OysterCardEvent> lastOysterCardEvent = this.cardEventRepository.getLastTapInTapOutEvent(cardId);
        if(!lastOysterCardEvent.isPresent() || OysterCardEventType.TAP_IN != lastOysterCardEvent.get().getCardEventType()) {
            System.out.println("Error! The had not tapped in his card");
        }
        Optional<Station> sourceStation = this.stationRepository.get(lastOysterCardEvent.get().getStationId());
        return fareProccessingStratergy.charge(oysterCard.get(), sourceStation.get(), station.get());
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
        OysterCard oysterCard = chargeTapOut(cardId, stationId);
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
