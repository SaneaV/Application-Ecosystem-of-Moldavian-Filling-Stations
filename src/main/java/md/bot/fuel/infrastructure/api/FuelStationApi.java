package md.bot.fuel.infrastructure.api;

import static md.bot.fuel.infrastructure.utils.CoordinatesConverter.convertWgs84ToUtm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.locationtech.proj4j.util.Pair;

@Getter
public class FuelStationApi {

  private final String name;
  private final Double petrol;
  private final Double diesel;
  private final Double gas;
  private final Pair<Double, Double> coordinates;

  @JsonCreator
  public FuelStationApi(
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
