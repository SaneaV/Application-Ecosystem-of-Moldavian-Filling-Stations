package md.fuel.api.rest.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import md.fuel.api.domain.FuelType;
import md.fuel.api.infrastructure.validation.ValueOfEnum;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.exception.GatewayErrorDescription;
import md.fuel.api.rest.exception.RfcErrorDescription;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Validated
@OpenAPIDefinition(info = @Info(title = "Moldova Filling Station API", description =
    "Description of available APIs for interacting with filling stations in Moldova.", version = "v1"))
@Tags(value = @Tag(name = "Filling Station Controller",
    description = "A group of controllers to work with filling station data."))
public interface FillingStationController {

  String FUEL_TYPE_PATH_PARAM = "fuel-type";

  @Operation(summary = "Get all filling stations in specific radius.", description =
      "Returns all filling stations within a certain radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = FillingStationDto.class)))}),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})
  })
  @GetMapping(value = "/filling-station")
  ResponseEntity<List<FillingStationDto>> getAllFillingStations(@Valid LimitFillingStationRequest request);

  @Operation(summary = "Get page of all filling stations within a certain radius.", description = "Returns all filling stations "
      + "within a certain radius in page format.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", useReturnTypeSchema = true),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})
  })
  @GetMapping(value = "/page/filling-station")
  ResponseEntity<PageDto<FillingStationDto>> getPageOfAllFillingStations(@Valid LimitFillingStationRequest request,
      @Valid PageRequest pageRequest);

  @Operation(summary = "Get nearest filling station within a certain radius.", description =
      "Returns nearest filling station within a certain radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = FillingStationDto.class))}),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})
  })
  @GetMapping(value = "/filling-station/nearest")
  ResponseEntity<FillingStationDto> getNearestFillingStation(@Valid BaseFillingStationRequest request);

  @Operation(summary = "Get filling stations with the best specified fuel type within a certain radius.", description = "Return "
      + "filling stations with the best specified fuel type within a certain radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = FillingStationDto.class)))}),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})
  })
  @GetMapping(value = "/filling-station/{fuel-type}")
  ResponseEntity<List<FillingStationDto>> getBestFuelPrice(@Valid LimitFillingStationRequest request,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM)
      @ValueOfEnum(enumClass = FuelType.class, message = "The fuel type must be one of the following: Petrol, Diesel, Gas")
      @Parameter(name = "fuel-type", description = "Type of fuel for price search.", required = true, schema =
      @Schema(allowableValues = {"Petrol", "Diesel", "Gas"}))
      String fuelType);

  @Operation(summary = "Get page of filling stations with the best specified fuel type within a certain radius.", description =
      "Return filling stations with the best specified fuel type within a certain radius in page format.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", useReturnTypeSchema = true),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})
  })
  @GetMapping(value = "/page/filling-station/{fuel-type}")
  ResponseEntity<PageDto<FillingStationDto>> getPageOfBestFuelPrice(@Valid LimitFillingStationRequest request,
      @Valid PageRequest pageRequest,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM)
      @ValueOfEnum(enumClass = FuelType.class, message = "The fuel type must be one of the following: Petrol, Diesel, Gas")
      @Parameter(name = "fuel-type", description = "Type of fuel for price search.", required = true, schema =
      @Schema(allowableValues = {"Petrol", "Diesel", "Gas"}))
      String fuelType);

  @Operation(summary = "Get last update timestamp.", description = "Return last update timestamp.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ZonedDateTime.class))}),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})})
  @GetMapping(value = "/filling-station/last-update")
  ResponseEntity<ZonedDateTime> getLastUpdateTimestamp();

  @Operation(summary = "Get list of supporting fuel types.", description = "Return list of supporting fuel types.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = {GatewayErrorDescription.class,
              RfcErrorDescription.class}))})})
  @GetMapping(value = "/filling-station/fuel-type")
  ResponseEntity<List<String>> getAvailableFuelTypes();
}
