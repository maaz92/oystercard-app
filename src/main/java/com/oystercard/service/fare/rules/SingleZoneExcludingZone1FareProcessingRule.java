package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;
import com.oystercard.entity.Zone;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SingleZoneExcludingZone1FareProcessingRule implements FareProcessingRule {

    public static final double FARE = 2D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {

        Set<Zone> sourceStationZones =  new HashSet<>(sourceStation.getZones());
        for(Zone destinationZone: destinationStation.getZones()) {
            if(destinationZone != Zone.ZONE_1 && sourceStationZones.contains(destinationZone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
