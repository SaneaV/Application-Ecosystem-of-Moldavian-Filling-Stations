package md.electric.api.infrastructure.client.plugshare;

import java.util.List;
import md.electric.api.domain.ElectricStation;

public interface PlugShareClient {

  List<ElectricStation> fetchStations(Double latitude, Double longitude);
}