package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.entity.ZoneType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Zone1FareProcessingRule implements FareProcessingRule {

    public static final double FARE = 2.5D;

    @Override
    public boolean doesApply(Station sourceStation, Station destinationStation) {
        return sourceStation.getZones().contains(ZoneType.ZONE_1.getId()) && destinationStation.getZones().contains(ZoneType.ZONE_1.getId());
    }

    @Override
    public double getFare() {
        return FARE;
    }
}
