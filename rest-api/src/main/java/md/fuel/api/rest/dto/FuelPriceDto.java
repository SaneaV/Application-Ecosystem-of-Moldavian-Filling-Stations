package md.fuel.api.rest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "ANRE official fuel prices.")
public class FuelPriceDto {

  @Schema(description = "Price date.", example = "2023-09-08", requiredMode = REQUIRED)
  private final String date;
  @Schema(description = "ANRE official petrol price.", example = "10.0", requiredMode = REQUIRED)
  private final Double petrol;
  @Schema(description = "ANRE official diesel price.", example = "10.0", requiredMode = REQUIRED)
  private final Double diesel;
}