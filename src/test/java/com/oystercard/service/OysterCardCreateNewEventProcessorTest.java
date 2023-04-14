package com.oystercard.service;

import com.oystercard.entity.OysterCard;
import com.oystercard.entity.OysterCardEvent;
import com.oystercard.entity.OysterCardEventType;
import com.oystercard.repository.CardEventRepository;
import com.oystercard.repository.OysterCardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OysterCardCreateNewEventProcessorTest {

    @Mock
    private OysterCardRepository oysterCardRepository;

    @Mock
    private CardEventRepository cardEventRepository;

    @Test
    public void testProcess_HappyCase() {
        OysterCardCreateNewEventProcessor oysterCardCreateNewEventProcessor = new OysterCardCreateNewEventProcessor(oysterCardRepository, cardEventRepository);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardEventType(OysterCardEventType.CREATE);
        Mockito.when(oysterCardRepository.create()).thenReturn(new OysterCard());
        OysterCard oysterCard = oysterCardCreateNewEventProcessor.process(oysterCardEvent);
        Mockito.verify(oysterCardRepository, Mockito.times(1)).create();
        Mockito.verify(cardEventRepository, Mockito.times(1)).create(oysterCardEvent);
    }
}
