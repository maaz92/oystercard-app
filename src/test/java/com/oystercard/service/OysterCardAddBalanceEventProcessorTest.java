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
public class OysterCardAddBalanceEventProcessorTest {

    @Mock
    private OysterCardRepository oysterCardRepository;

    @Mock
    private CardEventRepository cardEventRepository;

    @Test
    public void testProcess_HappyCase() {
        OysterCardAddBalanceEventProcessor oysterCardAddBalanceEventProcessor = new OysterCardAddBalanceEventProcessor(oysterCardRepository, cardEventRepository);
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardId(1L);
        oysterCardEvent.setCardEventType(OysterCardEventType.ADD_BALANCE);
        oysterCardEvent.setAmount(30D);
        Mockito.when(oysterCardRepository.addBalance(oysterCardEvent.getCardId(), oysterCardEvent.getAmount())).thenReturn(new OysterCard());
        oysterCardAddBalanceEventProcessor.process(oysterCardEvent);
        Mockito.verify(oysterCardRepository, Mockito.times(1)).addBalance(oysterCardEvent.getCardId(), oysterCardEvent.getAmount());
        Mockito.verify(cardEventRepository, Mockito.times(1)).create(oysterCardEvent);
    }
}
