package md.fuel.api.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FuelType {

  PETROL("Petrol"),
  DIESEL("Diesel"),
  GAS("Gas");

  private final String description;
}