package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.entity.Station;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import com.oystercard.repository.StationRepository;
import com.oystercard.service.fare.FareProcessingStratergy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OysterCardTapInEventProcessorTest {

    @Mock
    private OysterCardRepository oysterCardRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private CardEventRepository cardEventRepository;

    @Mock
    private FareProcessingStratergy fareProccessingStratergy;

    @Test
    public void testProcess_HappyCase() {
        OysterCardTapInEventProcessor oysterCardTapInEventProcessor = new OysterCardTapInEventProcessor(oysterCardRepository, stationRepository, cardEventRepository, fareProccessingStratergy);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardId(1L);
        oysterCardEvent.setCardEventType(OysterCardEventType.TAP_IN);
        oysterCardEvent.setStationId(2L);
        OysterCard oysterCard = new OysterCard();
        oysterCard.setId(1L);
        Station station = new Station();
        station.setId(2L);
        Mockito.when(oysterCardRepository.get(oysterCardEvent.getCardId())).thenReturn(Optional.of(oysterCard));
        Mockito.when(stationRepository.get(oysterCardEvent.getStationId())).thenReturn(Optional.of(station));
        Mockito.when(oysterCardRepository.reserveBalance(oysterCardEvent.getCardId(), 0D)).thenReturn(oysterCard);
        oysterCardTapInEventProcessor.process(oysterCardEvent);
        Mockito.verify(oysterCardRepository, Mockito.times(1)).get(oysterCardEvent.getCardId());
        Mockito.verify(stationRepository, Mockito.times(1)).get(oysterCardEvent.getStationId());
        Mockito.verify(oysterCardRepository, Mockito.times(1)).reserveBalance(oysterCardEvent.getCardId(), 0D);
    }
}
