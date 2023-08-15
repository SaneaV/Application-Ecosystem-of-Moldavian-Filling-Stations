package md.bot.fuel.facade.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuelStationDto {

  public static String timestamp;

  @Schema(description = "Fuel Station Name.", example = "Fuel-Station", requiredMode = REQUIRED)
  private final String name;
  @Schema(description = "Petrol price.", example = "10.0", requiredMode = REQUIRED)
  private final Double petrol;
  @Schema(description = "Diesel price.", example = "15.0", requiredMode = REQUIRED)
  private final Double diesel;
  @Schema(description = "Gas price.", example = "20.0", requiredMode = REQUIRED)
  private final Double gas;
  @Schema(description = "Fuel station latitude.", example = "46.0", requiredMode = REQUIRED)
  private final double latitude;
  @Schema(description = "Fuel station longitude.", example = "28.0", requiredMode = REQUIRED)
  private final double longitude;
}
