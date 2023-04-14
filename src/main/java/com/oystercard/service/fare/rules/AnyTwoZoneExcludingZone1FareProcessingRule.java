package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;
import com.oystercard.entity.Zone;
import org.springframework.stereotype.Component;

@Component
public class AnyTwoZoneExcludingZone1FareProcessingRule implements FareProcessingRule{

    public static final double FARE = 2.25D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {

        long sourceStationRequiredZonesCount = sourceStation.getZones().stream().filter(zone -> Zone.ZONE_1 != zone).count();
        long destinationStationRequiredZonesCount = destinationStation.getZones().stream().filter(zoneId -> Zone.ZONE_1 != zoneId).count();
        return sourceStationRequiredZonesCount > 1 || destinationStationRequiredZonesCount > 1;
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
