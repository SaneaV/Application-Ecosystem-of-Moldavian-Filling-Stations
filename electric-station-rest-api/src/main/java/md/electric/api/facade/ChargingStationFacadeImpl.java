package md.electric.api.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.electric.api.infrastructure.client.ChargingStationClient;
import md.electric.api.rest.dto.ChargingStationDto;
import md.electric.api.rest.mapper.ChargingStationDtoMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargingStationFacadeImpl implements ChargingStationFacade {

  private final ChargingStationClient chargingStationClient;
  private final ChargingStationDtoMapper mapper;

  @Override
  public List<ChargingStationDto> getAllChargingStations(Double latitude, Double longitude) {
    return chargingStationClient.fetchStations(latitude, longitude).stream()
        .map(mapper::toDto)
        .toList();
  }
}