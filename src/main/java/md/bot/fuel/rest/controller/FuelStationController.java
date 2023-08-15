package md.bot.fuel.rest.controller;

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
import java.util.List;
import javax.validation.Valid;
import md.bot.fuel.domain.FuelType;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.infrastructure.validation.ValueOfEnum;
import md.bot.fuel.rest.exception.GatewayErrorDescription;
import md.bot.fuel.rest.exception.RfcErrorDescription;
import md.bot.fuel.rest.request.FuelStationRequest;
import md.bot.fuel.rest.request.PageRequest;
import md.bot.fuel.rest.wrapper.PageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;

@Validated
@OpenAPIDefinition(info = @Info(title = "Moldova Fuel Station API", description =
    "Description of available APIs for interacting with fuel stations in Moldova.", version = "v1"))
@Tags(value = @Tag(name = "Fuel Station Controller", description = "A group of controllers to work with fuel station data."))
public interface FuelStationController {

  String FUEL_TYPE_PATH_PARAM = "fuel-type";

  @Operation(summary = "Get all fuel stations in specific radius.", description = "Returns all fuel stations within a certain "
      + "radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FuelStationDto.class)))}),
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
  @GetMapping(value = "/fuel-station")
  ResponseEntity<List<FuelStationDto>> getAllFuelStations(@Valid FuelStationRequest request, WebRequest webRequest);

  @Operation(summary = "Get page of all fuel stations within a certain radius.", description = "Returns all fuel stations "
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
  @GetMapping(value = "page/fuel-station")
  ResponseEntity<PageDto<FuelStationDto>> getPageOfAllFuelStations(@Valid FuelStationRequest request,
      @Valid PageRequest pageRequest, WebRequest webRequest);

  @Operation(summary = "Get nearest fuel station within a certain radius.", description = "Returns nearest fuel station within "
      + "a certain radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = FuelStationDto.class))}),
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
  @GetMapping(value = "/fuel-station/nearest")
  ResponseEntity<FuelStationDto> getNearestFuelStation(@Valid FuelStationRequest request, WebRequest webRequest);

  @Operation(summary = "Get fuel stations with the best specified fuel type within a certain radius.", description = "Return "
      + "fuel stations with the best specified fuel type within a certain radius.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK.", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FuelStationDto.class)))}),
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
  @GetMapping(value = "/fuel-station/{fuel-type}")
  ResponseEntity<List<FuelStationDto>> getBestFuelPrice(@Valid FuelStationRequest request,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM)
      @ValueOfEnum(enumClass = FuelType.class, message = "The fuel type must be one of the following: Petrol, Diesel, Gas")
      @Parameter(name = "fuel-type", description = "Type of fuel for price search.", required = true, schema =
      @Schema(allowableValues = {"Petrol", "Diesel", "Gas"}))
      String fuelType,
      WebRequest webRequest);

  @Operation(summary = "Get page of fuel stations with the best specified fuel type within a certain radius.", description =
      "Return fuel stations with the best specified fuel type within a certain radius in page format.")
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
  @GetMapping(value = "page/fuel-station/{fuel-type}")
  ResponseEntity<PageDto<FuelStationDto>> getPageOfBestFuelPrice(@Valid FuelStationRequest request,
      @Valid PageRequest pageRequest,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM)
      @ValueOfEnum(enumClass = FuelType.class, message = "The fuel type must be one of the following: Petrol, Diesel, Gas")
      @Parameter(name = "fuel-type", description = "Type of fuel for price search.", required = true, schema =
      @Schema(allowableValues = {"Petrol", "Diesel", "Gas"}))
      String fuelType,
      WebRequest webRequest);
}
