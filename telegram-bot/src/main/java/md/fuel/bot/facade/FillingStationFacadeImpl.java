package md.fuel.bot.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingStationFacadeImpl implements FillingStationFacade {

  private final FillingStationRepository fillingStationRepository;

  @Override
  public FillingStation getAllFillingStations(double latitude, double longitude, double radius, int offset) {
    log.info("Fetching list of filling stations with coordinates x = {}, y = {}, radius = {} (meters), offset = {}", latitude,
        longitude, radius, offset);

    updateTimeStamp();
    return fillingStationRepository.getAllFillingStation(latitude, longitude, radius)
        .getItems()
        .get(offset);
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    log.info("Fetching nearest filling stations with coordinates x = {}, y = {} and radius = {} (meters)", latitude, longitude,
        radius);

    updateTimeStamp();
    return fillingStationRepository.getNearestFillingStation(latitude, longitude, radius);
  }

  @Override
  public FillingStation getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int offset) {
    log.info(
        "Fetching list of filling stations with coordinates x = {}, y = {}, radius = {} (meters), offset = {} with best fuel price for {}",
        latitude, longitude, radius, offset, fuelType);

    updateTimeStamp();
    return fillingStationRepository.getBestFuelPriceStation(latitude, longitude, radius, fuelType)
        .getItems()
        .get(offset);
  }

  @Override
  public FuelType getSupportedFuelTypes() {
    log.info("Fetch list of supported fuel types");

    return fillingStationRepository.getSupportedFuelTypes();
  }

  @Override
  public boolean hasNext(double latitude, double longitude, double radius, int offset) {
    log.info("Check if next filling station exist with coordinates x = {}, y = {}, radius = {} (meters), offset = {}", latitude,
        longitude, radius, offset);

    return fillingStationRepository.getAllFillingStation(latitude, longitude, radius).getTotalResults() > offset + 1;
  }

  @Override
  public boolean hasNext(double latitude, double longitude, double radius, int offset, String fuelType) {
    log.info(
        "Check if next filling station exist with coordinates x = {}, y = {}, radius = {} (meters), offset = {} for best fuel price request",
        latitude, longitude, radius, offset);

    return fillingStationRepository.getBestFuelPriceStation(latitude, longitude, radius, fuelType).getTotalResults() > offset + 1;
  }

  private void updateTimeStamp() {
    log.info("Update last modification timestamp");

    FillingStation.timestamp = fillingStationRepository.getUpdateTimestamp();
  }
}
