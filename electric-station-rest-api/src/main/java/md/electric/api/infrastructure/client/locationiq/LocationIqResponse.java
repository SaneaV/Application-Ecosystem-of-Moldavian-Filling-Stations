package md.electric.api.infrastructure.client.locationiq;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationIqResponse {

  private final Address address;

  @JsonCreator
  public LocationIqResponse(@JsonProperty("address") Address address) {
    this.address = address;
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Address {

    private final String district;
    private final String city;
    private final String countryCode;

    @JsonCreator
    public Address(
        @JsonProperty("district") String district,
        @JsonProperty("city") String city,
        @JsonProperty("country_code") String countryCode) {
      this.district = district;
      this.city = city;
      this.countryCode = countryCode;
    }
  }
}