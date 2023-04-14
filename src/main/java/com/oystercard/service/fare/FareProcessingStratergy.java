package com.citystoragesystems.service.fare;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.Station;

public interface FareProcessingStratergy {

    double calculateFare(Station sourceStation, Station destinationStation);
}
