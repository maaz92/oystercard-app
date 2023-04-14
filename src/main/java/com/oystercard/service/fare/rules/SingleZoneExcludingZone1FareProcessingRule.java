package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.entity.ZoneType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class SingleZoneExcludingZone1FareProcessingRule implements FareProcessingRule {

    public static final double FARE = 2D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {

        Set<Long> sourceStationZones =  new HashSet<>(sourceStation.getZones());
        for(long destinationZoneId: destinationStation.getZones()) {
            if(destinationZoneId != ZoneType.ZONE_1.getId() && sourceStationZones.contains(destinationZoneId)) {
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
