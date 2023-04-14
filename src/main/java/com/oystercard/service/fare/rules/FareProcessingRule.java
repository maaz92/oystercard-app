package com.oystercard.service.fare.rules;

import com.oystercard.entity.Station;

public interface FareProcessingRule {

    boolean doesApply(Station sourceStation, Station destinationStation);

    double getFare();
}
