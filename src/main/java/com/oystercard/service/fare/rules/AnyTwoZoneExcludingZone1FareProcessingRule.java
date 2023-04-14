package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.entity.ZoneType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnyTwoZoneExcludingZone1FareProcessingRule implements FareProcessingRule{

    public static final double FARE = 2.25D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {

        long sourceStationRequiredZonesCount = sourceStation.getZones().stream().filter(zoneId -> ZoneType.ZONE_1.getId() != zoneId).count();
        long destinationStationRequiredZonesCount = destinationStation.getZones().stream().filter(zoneId -> ZoneType.ZONE_1.getId() != zoneId).count();
        return sourceStationRequiredZonesCount > 1 || destinationStationRequiredZonesCount > 1;
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
