package md.fuel.api.rest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filling Station Data.")
public record FillingStationDto(
    @Schema(description = "Filling Station Name.", example = "Filling-Station", requiredMode = REQUIRED) String name,
    @Schema(description = "Petrol price.", example = "10.0", requiredMode = REQUIRED) Double petrol,
    @Schema(description = "Diesel price.", example = "15.0", requiredMode = REQUIRED) Double diesel,
    @Schema(description = "Gas price.", example = "20.0", requiredMode = REQUIRED) Double gas,
    @Schema(description = "Filling station latitude.", example = "46.0", requiredMode = REQUIRED) double latitude,
    @Schema(description = "Filling station longitude.", example = "28.0", requiredMode = REQUIRED) double longitude) {

  public static String timestamp;
}
