package md.fuel.api.infrastructure.repository;

import static md.fuel.api.infrastructure.utils.CoordinateConverter.convertWgs84ToUtm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.locationtech.proj4j.util.Pair;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FillingStationApi {

  private final String name;
  private final Double petrol;
  private final Double diesel;
  private final Double gas;
  private final Pair<Double, Double> coordinates;

  @JsonCreator
  public FillingStationApi(
      @JsonProperty(value = "station_name", required = true) String name,
      @JsonProperty(value = "gasoline", required = true) Double petrol,
      @JsonProperty(value = "diesel", required = true) Double diesel,
      @JsonProperty(value = "gpl", required = true) Double gas,
      @JsonProperty(value = "x", required = true) double latitude,
      @JsonProperty(value = "y", required = true) double longitude) {
    this.name = name;
    this.petrol = petrol;
    this.diesel = diesel;
    this.gas = gas;
    this.coordinates = convertWgs84ToUtm(latitude, longitude);
  }
}
