package md.fuel.api.rest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "Filling Station Data.")
public class FillingStationDto {

  public static String TIMESTAMP;

  @Schema(description = "Filling Station Name.", example = "Filling-Station", requiredMode = REQUIRED)
  private final String name;
  @Schema(description = "Petrol price.", example = "10.0", requiredMode = REQUIRED)
  private final Double petrol;
  @Schema(description = "Diesel price.", example = "15.0", requiredMode = REQUIRED)
  private final Double diesel;
  @Schema(description = "Gas price.", example = "20.0", requiredMode = REQUIRED)
  private final Double gas;
  @Schema(description = "Filling station latitude.", example = "46.0", requiredMode = REQUIRED)
  private final double latitude;
  @Schema(description = "Filling station longitude.", example = "28.0", requiredMode = REQUIRED)
  private final double longitude;
}