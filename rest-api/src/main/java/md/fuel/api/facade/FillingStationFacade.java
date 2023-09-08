package md.fuel.api.facade;

import java.time.ZonedDateTime;
import java.util.List;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FuelPriceDto;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;

public interface FillingStationFacade {

  List<FillingStationDto> getAllFillingStations(LimitFillingStationRequest request);

  FillingStationDto getNearestFillingStation(BaseFillingStationRequest request);

  List<FillingStationDto> getBestFuelPrice(LimitFillingStationRequest request, String fuelType);

  ZonedDateTime getLastUpdateTimestamp();

  List<String> getAvailableFuelTypes();

  FuelPriceDto getAnrePrices();
}
