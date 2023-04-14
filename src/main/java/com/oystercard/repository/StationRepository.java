package com.citystoragesystems.repository;

import com.citystoragesystems.entity.OysterCard;
import com.citystoragesystems.entity.Station;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StationRepository {

    Map<Long, Station> stations;

    public StationRepository() {
        this.stations = new HashMap<>();
    }

    public Optional<Station> get(long id) {
        Station station = stations.get(id);
        if(station==null) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(station.copy());
    }
}
