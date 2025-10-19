package md.electric.api.facade;

import java.util.List;
import md.electric.api.rest.dto.ChargingStationDto;

public interface ChargingStationFacade {

  List<ChargingStationDto> getAllChargingStations(Double latitude, Double longitude);
}