package com.oystercard.service.fare;

import com.oystercard.entity.Station;

public interface FareProcessingStratergy {

    double calculateFare(Station sourceStation, Station destinationStation);

    double calculateFareReserveAmount(Station sourceStation);
}
