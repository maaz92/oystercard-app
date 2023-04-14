package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.entity.ZoneType;
import org.springframework.stereotype.Component;

@Component
public class AnyTwoZoneIncludingZone1FareProcessingRule implements FareProcessingRule{

    public static final double FARE = 3D;
    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return (sourceStation.getZones().size() > 1 || destinationStation.getZones().size() > 1)
                && (sourceStation.getZones().contains(ZoneType.ZONE_1.getId()) || destinationStation.getZones().contains(ZoneType.ZONE_1.getId()));
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
