package md.fuel.api.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import md.fuel.api.rest.dto.FuelPriceDto;
import md.fuel.api.rest.exception.GatewayErrorDescription;
import md.fuel.api.rest.exception.RfcErrorDescription;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tags(value = @Tag(name = "ANRE official prices", description = "ANRE official prices for petrol and diesel."))
public interface AnrePriceController extends SwaggerController {

  @Operation(summary = "Get official ANRE fuel price.", description = "Return official ANRE petrol and diesel price.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FuelPriceDto.class)))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(title = "ErrorDescriptionResponse", oneOf = {
              GatewayErrorDescription.class, RfcErrorDescription.class}))})})
  @GetMapping(value = "/price")
  ResponseEntity<FuelPriceDto> getAnreFuelPrice();
}
