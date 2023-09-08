package md.fuel.api.rest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ANRE official fuel prices.")
public record FuelPriceDto(
    @Schema(description = "ANRE official petrol price.", example = "10.0", requiredMode = REQUIRED) Double petrol,
    @Schema(description = "ANRE official diesel price.", example = "10.0", requiredMode = REQUIRED) Double diesel,
    @Schema(description = "Price date.", example = "2023-09-08", requiredMode = REQUIRED) String date) {

}
