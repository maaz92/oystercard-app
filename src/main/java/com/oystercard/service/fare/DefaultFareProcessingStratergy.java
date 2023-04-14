package com.citystoragesystems.service.fare;

import com.citystoragesystems.entity.Station;
import com.citystoragesystems.service.fare.rules.FareProcessingRule;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DefaultFareProcessingStratergy implements FareProcessingStratergy {

    List<FareProcessingRule> fareProcessingRules;

    public DefaultFareProcessingStratergy(List<FareProcessingRule> fareProcessingRules) {
        this.fareProcessingRules = fareProcessingRules;
    }

    @Override
    public double calculateFare(Station sourceStation, Station destinationStation) {
        return fareProcessingRules.stream().filter(fairProcessingRule -> fairProcessingRule.doesApply(sourceStation, destinationStation)).map(fareProcessingRule -> fareProcessingRule.getFare()).min(Double::compareTo).orElseThrow(() -> new RuntimeException("No fare rules are applicable"));
    }
}
