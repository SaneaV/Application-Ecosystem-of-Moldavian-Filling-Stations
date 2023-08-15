package md.bot.fuel.rest.controller;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.rest.request.FuelStationRequest;
import md.bot.fuel.rest.request.PageRequest;
import md.bot.fuel.rest.wrapper.FuelStationPageWrapper;
import md.bot.fuel.rest.wrapper.PageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
public class FuelStationControllerImpl implements FuelStationController {

  private static final String CLIENT_ATTRIBUTE = "CLIENT";
  private static final String CLIENT_PARAM = "REST";
  private static final String FUEL_TYPE_PATH_PARAM = "fuel-type";

  private final FuelStationFacade fuelStationFacade;
  private final FuelStationPageWrapper fuelStationPageWrapper;

  @GetMapping(value = "/fuel-station")
  public ResponseEntity<List<FuelStationDto>> getAllFuelStations(FuelStationRequest request, WebRequest webRequest) {
    setExceptionHandlerClient(webRequest);
    final List<FuelStationDto> allFuelStations = fuelStationFacade.getAllFuelStations(request.getLatitude(),
        request.getLongitude(), request.getRadius(), request.getLimitInRadius());

    return ResponseEntity.ok().body(allFuelStations);
  }

  @GetMapping(value = "page/fuel-station")
  public ResponseEntity<PageDto<FuelStationDto>> getPageOfAllFuelStations(FuelStationRequest request, PageRequest pageRequest,
      WebRequest webRequest) {
    setExceptionHandlerClient(webRequest);
    final PageDto<FuelStationDto> fuelStationDtoPageDto = fuelStationPageWrapper.wrapAllFuelStationList(request.getLatitude(),
        request.getLongitude(), request.getRadius(), request.getLimitInRadius(), pageRequest.getLimit(), pageRequest.getOffset());

    return ResponseEntity.ok().body(fuelStationDtoPageDto);
  }

  @GetMapping(value = "/fuel-station/nearest")
  public ResponseEntity<FuelStationDto> getNearestFuelStation(FuelStationRequest request, WebRequest webRequest) {
    setExceptionHandlerClient(webRequest);
    final FuelStationDto nearestFuelStation = fuelStationFacade.getNearestFuelStation(request.getLatitude(),
        request.getLongitude(), request.getRadius());

    return ResponseEntity.ok().body(nearestFuelStation);
  }

  @GetMapping(value = "/fuel-station/{fuel-type}")
  public ResponseEntity<List<FuelStationDto>> getBestFuelPrice(FuelStationRequest request,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType, WebRequest webRequest) {
    setExceptionHandlerClient(webRequest);
    final List<FuelStationDto> allFuelStations = fuelStationFacade.getBestFuelPrice(request.getLatitude(), request.getLongitude(),
        request.getRadius(), fuelType, request.getLimitInRadius());

    return ResponseEntity.ok().body(allFuelStations);
  }

  @GetMapping(value = "page/fuel-station/{fuel-type}")
  public ResponseEntity<PageDto<FuelStationDto>> getPageOfBestFuelPrice(FuelStationRequest request, PageRequest pageRequest,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType, WebRequest webRequest) {
    setExceptionHandlerClient(webRequest);
    final PageDto<FuelStationDto> fuelStationDtoPageDto = fuelStationPageWrapper.wrapBestFuelPrice(request.getLatitude(),
        request.getLongitude(), request.getRadius(), fuelType, request.getLimitInRadius(), pageRequest.getLimit(),
        pageRequest.getOffset());

    return ResponseEntity.ok().body(fuelStationDtoPageDto);
  }

  private void setExceptionHandlerClient(WebRequest webRequest) {
    webRequest.setAttribute(CLIENT_ATTRIBUTE, CLIENT_PARAM, SCOPE_REQUEST);
  }
}