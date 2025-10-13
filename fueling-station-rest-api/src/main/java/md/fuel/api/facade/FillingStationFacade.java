package md.fuel.api.facade;

import java.time.ZonedDateTime;
import java.util.List;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FuelPriceDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;

public interface FillingStationFacade {

  PageDto<FillingStationDto> getPageOfFillingStations(LimitFillingStationRequest limitFillingStationRequest,
      PageRequest pageRequest);

  List<FillingStationDto> getAllFillingStations(LimitFillingStationRequest request);

  List<FillingStationDto> getAllFillingStations();

  FillingStationDto getNearestFillingStation(BaseFillingStationRequest request);

  List<FillingStationDto> getBestFuelPrice(LimitFillingStationRequest request, String fuelType);

  PageDto<FillingStationDto> getPageOfBestFuelPrices(LimitFillingStationRequest limitFillingStationRequest,
      PageRequest pageRequest, String fuelType);

  ZonedDateTime getLastUpdateTimestamp();

  List<String> getAvailableFuelTypes();

  FuelPriceDto getAnrePrices();
}