package com.oystercard.entity;

import java.io.Serializable;

public class OysterCardEvent implements Serializable {

    private Long cardId;

    private OysterCardEventType cardEventType;

    private Long stationId;

    private Double amount;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public OysterCardEventType getCardEventType() {
        return cardEventType;
    }

    public void setCardEventType(OysterCardEventType cardEventType) {
        this.cardEventType = cardEventType;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OysterCardEvent copy() {
        OysterCardEvent oysterCardEvent = new OysterCardEvent();
        oysterCardEvent.setCardId(this.getCardId());
        oysterCardEvent.setStationId(this.getStationId());
        oysterCardEvent.setCardEventType(this.getCardEventType());
        oysterCardEvent.setAmount(this.getAmount());
        return oysterCardEvent;
    }
}
