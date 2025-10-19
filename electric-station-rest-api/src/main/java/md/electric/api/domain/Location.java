package md.electric.api.domain;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Location implements Serializable {

  private final String country;
  private final String city;
  private final String district;
  private final Double latitude;
  private final Double longitude;
}