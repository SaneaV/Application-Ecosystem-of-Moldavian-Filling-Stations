package md.fuel.api.rest.controller;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import md.fuel.api.rest.wrapper.FillingStationPageWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FillingStationControllerImpl implements FillingStationController {

  private final FillingStationFacade fillingStationFacade;
  private final FillingStationPageWrapper fillingStationPageWrapper;

  @Override
  public ResponseEntity<List<FillingStationDto>> getAllFillingStations(LimitFillingStationRequest request) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(request);

    return ResponseEntity.ok().body(allFillingStations);
  }

  @Override
  public ResponseEntity<PageDto<FillingStationDto>> getPageOfAllFillingStations(LimitFillingStationRequest fillingStationRequest,
      PageRequest pageRequest) {
    final PageDto<FillingStationDto> fillingStationDtoPageDto = fillingStationPageWrapper.wrapAllFillingStationsList(
        fillingStationRequest, pageRequest);

    return ResponseEntity.ok().body(fillingStationDtoPageDto);
  }

  @Override
  public ResponseEntity<FillingStationDto> getNearestFillingStation(BaseFillingStationRequest request) {
    final FillingStationDto nearestFillingStation = fillingStationFacade.getNearestFillingStation(request);

    return ResponseEntity.ok().body(nearestFillingStation);
  }

  @Override
  public ResponseEntity<List<FillingStationDto>> getBestFuelPrice(LimitFillingStationRequest request,
      @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getBestFuelPrice(request, fuelType);

    return ResponseEntity.ok().body(allFillingStations);
  }

  @Override
  public ResponseEntity<PageDto<FillingStationDto>> getPageOfBestFuelPrice(LimitFillingStationRequest request,
      PageRequest pageRequest, @PathVariable(value = FUEL_TYPE_PATH_PARAM) String fuelType) {
    final PageDto<FillingStationDto> fillingStationDtoPageDto = fillingStationPageWrapper.wrapBestFuelPriceStation(request,
        pageRequest, fuelType);

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