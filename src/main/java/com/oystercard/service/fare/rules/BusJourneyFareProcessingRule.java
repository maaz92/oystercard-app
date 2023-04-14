package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;
import org.springframework.stereotype.Component;

@Component
public class BusJourneyFareProcessingRule implements FareProcessingRule{

    public static final double FARE = 1.8D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return sourceStation.getType() == Station.Type.BUS && destinationStation.getType() == Station.Type.BUS;
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
