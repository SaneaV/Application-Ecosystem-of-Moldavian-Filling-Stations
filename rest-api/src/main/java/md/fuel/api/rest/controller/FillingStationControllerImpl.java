package md.fuel.api.rest.controller;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import md.fuel.api.rest.wrapper.FillingStationPageWrapper;
import md.fuel.api.rest.wrapper.PageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
public class FillingStationControllerImpl implements FillingStationController {

  private final FillingStationFacade fillingStationFacade;
  private final FillingStationPageWrapper fillingStationPageWrapper;

  @Override
  public ResponseEntity<List<FillingStationDto>> getAllFillingStations(LimitFillingStationRequest request,
      WebRequest webRequest) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(request.getLatitude(),
        request.getLongitude(), request.getRadius(), request.getLimitInRadius());

    return ResponseEntity.ok().body(allFillingStations);
  }

  @Override
  public ResponseEntity<PageDto<FillingStationDto>> getPageOfAllFillingStations(LimitFillingStationRequest request,
      PageRequest pageRequest, WebRequest webRequest) {
    final PageDto<FillingStationDto> fillingStationDtoPageDto = fillingStationPageWrapper.wrapAllFillingStationsList(
        request.getLatitude(), request.getLongitude(), request.getRadius(), request.getLimitInRadius(), pageRequest.getLimit(),
        pageRequest.getOffset());

    return ResponseEntity.ok().body(fillingStationDtoPageDto);
  }

  @Override
  public ResponseEntity<FillingStationDto> getNearestFillingStation(BaseFillingStationRequest request, WebRequest webRequest) {
    final FillingStationDto nearestFillingStation = fillingStationFacade.getNearestFillingStation(request.getLatitude(),
        request.getLongitude(), request.getRadius());

    return ResponseEntity.ok().body(nearestFillingStation);
  }

  @Override
  public ResponseEntity<List<FillingStationDto>> getBestFuelPrice(LimitFillingStationRequest request,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType, WebRequest webRequest) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getBestFuelPrice(request.getLatitude(),
        request.getLongitude(), request.getRadius(), fuelType, request.getLimitInRadius());

    return ResponseEntity.ok().body(allFillingStations);
  }

  @Override
  public ResponseEntity<PageDto<FillingStationDto>> getPageOfBestFuelPrice(LimitFillingStationRequest request,
      PageRequest pageRequest,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType, WebRequest webRequest) {
    final PageDto<FillingStationDto> fillingStationDtoPageDto = fillingStationPageWrapper.wrapBestFuelPriceStation(
        request.getLatitude(),
        request.getLongitude(), request.getRadius(), fuelType, request.getLimitInRadius(), pageRequest.getLimit(),
        pageRequest.getOffset());

    return ResponseEntity.ok().body(fillingStationDtoPageDto);
  }

  @Override
  public ResponseEntity<ZonedDateTime> getLastUpdateTimestamp() {
    final ZonedDateTime lastUpdateTimestamp = fillingStationFacade.getLastUpdateTimestamp();
    return ResponseEntity.ok().body(lastUpdateTimestamp);
  }

  @Override
  public ResponseEntity<List<String>> getAvailableFuelTypes() {
    return ResponseEntity.ok(fillingStationFacade.getAvailableFuelTypes());
  }
}