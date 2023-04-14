package com.oystercard.service.fare;

import com.oystercard.entity.Station;
import com.oystercard.service.fare.rules.FareProcessingRule;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DefaultFareProcessingStratergy implements FareProcessingStratergy {

    List<FareProcessingRule> fareProcessingRules;

    private static final double TUBE_MAXIMUM_FARE = 3.2D;

    private static final double BUS_MAXIMUM_FARE = 1.8D;

    public DefaultFareProcessingStratergy(List<FareProcessingRule> fareProcessingRules) {
        this.fareProcessingRules = fareProcessingRules;
    }

    @Override
    public double calculateFare(Station sourceStation, Station destinationStation) {
        return fareProcessingRules.stream().filter(fairProcessingRule -> fairProcessingRule.doesApply(sourceStation, destinationStation)).map(fareProcessingRule -> fareProcessingRule.getFare()).min(Double::compareTo).orElseThrow(() -> new RuntimeException("No fare rules are applicable"));
    }

    @Override
    public double calculateFareReserveAmount(Station sourceStation) {
        if(sourceStation.getType() == Station.Type.BUS) {
            return BUS_MAXIMUM_FARE;
        }
        return TUBE_MAXIMUM_FARE;
    }
}
