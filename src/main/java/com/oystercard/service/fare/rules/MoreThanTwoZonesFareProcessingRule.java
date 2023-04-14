package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.entity.ZoneType;
import org.springframework.stereotype.Component;

@Component
public class MoreThanTwoZonesFareProcessingRule implements FareProcessingRule{

    public static final double FARE = 3.2D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return !sourceStation.getZones().contains(ZoneType.ZONE_2.getId()) && !destinationStation.getZones().contains(ZoneType.ZONE_2.getId());
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
