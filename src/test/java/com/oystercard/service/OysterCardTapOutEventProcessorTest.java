package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.entity.Station;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import com.oystercard.repository.StationRepository;
import com.oystercard.service.fare.FareProcessingStratergy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OysterCardTapOutEventProcessorTest {

    @Mock
    private OysterCardRepository oysterCardRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private CardEventRepository cardEventRepository;

    @Mock
    private FareProcessingStratergy fareProccessingStratergy;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testProcess_HappyCase() {
        OysterCardTapOutEventProcessor oysterCardTapOutEventProcessor = new OysterCardTapOutEventProcessor(oysterCardRepository, stationRepository, cardEventRepository, fareProccessingStratergy);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardId(1L);
        oysterCardEvent.setCardEventType(OysterCardEventType.TAP_OUT);
        oysterCardEvent.setStationId(2L);
        OysterCard oysterCard = new OysterCard();
        oysterCard.setId(1L);
        Station station = new Station();
        station.setId(2L);
        OysterCardEvent lastTapInTapOutEvent = new OysterCardEvent();
        lastTapInTapOutEvent.setStationId(4L);
        lastTapInTapOutEvent.setCardEventType(OysterCardEventType.TAP_IN);
        Station lastTapInStation = new Station();
        lastTapInStation.setId(2L);
        Mockito.when(oysterCardRepository.get(oysterCardEvent.getCardId())).thenReturn(Optional.of(oysterCard));
        Mockito.when(stationRepository.get(oysterCardEvent.getStationId())).thenReturn(Optional.of(station));
        Mockito.when(cardEventRepository.getLastTapInTapOutEvent(oysterCardEvent.getCardId())).thenReturn(Optional.of(lastTapInTapOutEvent));
        Mockito.when(stationRepository.get(lastTapInTapOutEvent.getStationId())).thenReturn(Optional.of(lastTapInStation));
        Mockito.when(oysterCardRepository.deductFare(oysterCardEvent.getCardId(), 0D)).thenReturn(oysterCard);
        oysterCardTapOutEventProcessor.process(oysterCardEvent);
        Mockito.verify(oysterCardRepository, Mockito.times(1)).get(oysterCardEvent.getCardId());
        Mockito.verify(stationRepository, Mockito.times(1)).get(oysterCardEvent.getStationId());
        Mockito.verify(stationRepository, Mockito.times(1)).get(lastTapInTapOutEvent.getStationId());
        Mockito.verify(cardEventRepository, Mockito.times(1)).getLastTapInTapOutEvent(oysterCardEvent.getCardId());
        Mockito.verify(oysterCardRepository, Mockito.times(1)).deductFare(oysterCardEvent.getCardId(), 0D);
    }

    @Test
    public void testProcess_NoTapInForTapOutCase() {
        OysterCardTapOutEventProcessor oysterCardTapOutEventProcessor = new OysterCardTapOutEventProcessor(oysterCardRepository, stationRepository, cardEventRepository, fareProccessingStratergy);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardId(1L);
        oysterCardEvent.setCardEventType(OysterCardEventType.TAP_OUT);
        oysterCardEvent.setStationId(2L);
        OysterCard oysterCard = new OysterCard();
        oysterCard.setId(1L);
        Station station = new Station();
        station.setId(2L);
        Mockito.when(oysterCardRepository.get(oysterCardEvent.getCardId())).thenReturn(Optional.of(oysterCard));
        Mockito.when(stationRepository.get(oysterCardEvent.getStationId())).thenReturn(Optional.of(station));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Error! The user had not tapped in his card");
        oysterCardTapOutEventProcessor.process(oysterCardEvent);
    }
}
