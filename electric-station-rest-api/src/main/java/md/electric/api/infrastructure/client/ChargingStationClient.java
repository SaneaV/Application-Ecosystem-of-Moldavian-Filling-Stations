package md.electric.api.infrastructure.client;

import java.util.List;
import md.electric.api.domain.ChargingStation;

public interface ChargingStationClient {

  List<ChargingStation> fetchStations(Double latitude, Double longitude);
}