package md.electric.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChargingStationDto {

  private final String name;
  private final String city;
  private final String district;
  private final Double latitude;
  private final Double longitude;
  @JsonProperty("station_count")
  private final Integer stationCount;
  @JsonProperty("connector_types")
  private final Set<String> connectorTypes;
}