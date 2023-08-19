package md.fuel.api.facade;

import java.time.ZonedDateTime;
import java.util.List;
import md.fuel.api.rest.dto.FillingStationDto;

public interface FillingStationFacade {

  List<FillingStationDto> getAllFillingStations(double latitude, double longitude, double radius, int limit);

  FillingStationDto getNearestFillingStation(double latitude, double longitude, double radius);

  List<FillingStationDto> getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int limit);

  ZonedDateTime getLastUpdateTimestamp();
}
