package com.citystoragesystems.service.fare.rules;

import com.citystoragesystems.entity.Station;

public interface FareProcessingRule {

    boolean doesApply(Station sourceStation, Station destinationStation);

    double getFare();
}
