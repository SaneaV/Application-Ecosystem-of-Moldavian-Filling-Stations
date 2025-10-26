package md.fuel.api.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FuelPriceApi {

  private final Double petrol;
  private final Double diesel;
  private final String date;

  @JsonCreator
  public FuelPriceApi(
      @JsonProperty(value = "b_pc", required = true) Double petrol,
      @JsonProperty(value = "m_pc", required = true) Double diesel,
      @JsonProperty(value = "date", required = true) String date) {
    this.petrol = petrol;
    this.diesel = diesel;
    this.date = date;
  }
}