package md.fuel.api.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuelPrice implements Serializable {

  private final Double petrol;
  private final Double diesel;
  private final String date;
}