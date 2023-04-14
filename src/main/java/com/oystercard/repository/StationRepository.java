package com.oystercard.repository;

import com.oystercard.entity.Station;
import com.oystercard.entity.Zone;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StationRepository {

    Map<Long, Station> stations;

    private void createHolbornTubeStation() {
        Station HolbornTubeStation = new Station();
        HolbornTubeStation.setId(1L);
        HolbornTubeStation.setType(Station.Type.TUBE);
        HolbornTubeStation.setName("Holborn Tube Station");
        HolbornTubeStation.setZones(Arrays.asList(Zone.ZONE_1));
        stations.put(HolbornTubeStation.getId(), HolbornTubeStation);
    }

    private void createAldgateTubeStation() {
        Station AldgateTubeStation = new Station();
        AldgateTubeStation.setId(2L);
        AldgateTubeStation.setType(Station.Type.TUBE);
        AldgateTubeStation.setName("Aldgate Tube Station");
        AldgateTubeStation.setZones(Arrays.asList(Zone.ZONE_1));
        stations.put(AldgateTubeStation.getId(), AldgateTubeStation);
    }

    private void createEarlsCourtTubeStation() {
        Station EarlsCourtTubeStation = new Station();
        EarlsCourtTubeStation.setId(3L);
        EarlsCourtTubeStation.setType(Station.Type.TUBE);
        EarlsCourtTubeStation.setName("Earls Court Tube Station");
        EarlsCourtTubeStation.setZones(Arrays.asList(Zone.ZONE_1, Zone.ZONE_2));
        stations.put(EarlsCourtTubeStation.getId(), EarlsCourtTubeStation);
    }

    private void createHammersmithTubeStation() {
        Station HammersmithTubeStation = new Station();
        HammersmithTubeStation.setId(4L);
        HammersmithTubeStation.setType(Station.Type.TUBE);
        HammersmithTubeStation.setName("Hammersmith Tube Station");
        HammersmithTubeStation.setZones(Arrays.asList(Zone.ZONE_2));
        stations.put(HammersmithTubeStation.getId(), HammersmithTubeStation);
    }

    private void createArsenalTubeStation() {
        Station ArsenalTubeStation = new Station();
        ArsenalTubeStation.setId(5L);
        ArsenalTubeStation.setType(Station.Type.TUBE);
        ArsenalTubeStation.setName("Arsenal Tube Station");
        ArsenalTubeStation.setZones(Arrays.asList(Zone.ZONE_2));
        stations.put(ArsenalTubeStation.getId(), ArsenalTubeStation);
    }

    private void createWimbledonTubeStation() {
        Station WimbledonTubeStation = new Station();
        WimbledonTubeStation.setId(6L);
        WimbledonTubeStation.setType(Station.Type.TUBE);
        WimbledonTubeStation.setName("Wimbledon Tube Station");
        WimbledonTubeStation.setZones(Arrays.asList(Zone.ZONE_3));
        stations.put(WimbledonTubeStation.getId(), WimbledonTubeStation);
    }

    private void createEarlsCourtBusStation() {
        Station EarlsCourtBusStation = new Station();
        EarlsCourtBusStation.setId(7L);
        EarlsCourtBusStation.setType(Station.Type.BUS);
        EarlsCourtBusStation.setName("Earls Court Bus Station");
        EarlsCourtBusStation.setZones(Collections.emptyList());
        stations.put(EarlsCourtBusStation.getId(), EarlsCourtBusStation);
    }

    private void createChelseaBusStation() {
        Station ChelseaBusStation = new Station();
        ChelseaBusStation.setId(8L);
        ChelseaBusStation.setType(Station.Type.BUS);
        ChelseaBusStation.setName("Chelsea Bus Station");
        ChelseaBusStation.setZones(Collections.emptyList());
        stations.put(ChelseaBusStation.getId(), ChelseaBusStation);
    }

    public StationRepository() {
        stations = new HashMap<>();
        createHolbornTubeStation();
        createAldgateTubeStation();
        createEarlsCourtTubeStation();
        createEarlsCourtBusStation();
        createChelseaBusStation();
        createHammersmithTubeStation();
        createArsenalTubeStation();
        createWimbledonTubeStation();
    }

    public Optional<Station> get(long id) {
        Station station = stations.get(id);
        if(station==null) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(station.copy());
    }

    public void printStationInputs() {
        this.stations.values().stream().forEach(station -> System.out.println(station.getId()+ " for " + station.getName()));
    }

}
