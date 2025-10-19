package md.electric.api.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChargingStationResponse {

  private final String name;
  private final Double latitude;
  private final Double longitude;
  private final int stationCount;
  private final String[] connectorTypes;

  @JsonCreator
  public ChargingStationResponse(
      @JsonProperty(value = "name", required = true) String name,
      @JsonProperty(value = "latitude", required = true) double latitude,
      @JsonProperty(value = "longitude", required = true) double longitude,
      @JsonProperty(value = "station_count", required = true) int stationCount,
      @JsonProperty(value = "connector_types", required = true) String[] connectorTypes) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.stationCount = stationCount;
    this.connectorTypes = connectorTypes;
  }
}