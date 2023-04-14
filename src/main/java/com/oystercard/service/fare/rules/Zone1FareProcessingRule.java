package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;
import com.oystercard.entity.Zone;
import org.springframework.stereotype.Component;

@Component
public class Zone1FareProcessingRule implements FareProcessingRule {

    public static final double FARE = 2.5D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return sourceStation.getZones().contains(Zone.ZONE_1) && destinationStation.getZones().contains(Zone.ZONE_1);
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
