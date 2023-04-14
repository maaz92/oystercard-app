package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;
import com.oystercard.entity.Zone;
import org.springframework.stereotype.Component;

@Component
public class MoreThanTwoZonesFareProcessingRule implements FareProcessingRule{

    public static final double FARE = 3.2D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return !sourceStation.getZones().contains(Zone.ZONE_2) && !destinationStation.getZones().contains(Zone.ZONE_2);
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
