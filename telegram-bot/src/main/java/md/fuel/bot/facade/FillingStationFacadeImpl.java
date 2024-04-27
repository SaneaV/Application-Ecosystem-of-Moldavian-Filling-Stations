package md.fuel.bot.facade;

import java.util.List;
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

  private static final int STANDARD_OFFSET = 0;

  private final FillingStationRepository fillingStationRepository;

  @Override
  public List<FillingStation> getAllFillingStations(double latitude, double longitude, double radius, int limitInRadius,
      int limit) {
    log.info(
        "Fetching list of filling stations with coordinates x = {}, y = {}, radius = {} (meters), limitInRadius = {} and pageLimit = {}",
        latitude, longitude, radius, limitInRadius, limit);

    updateTimeStamp();
    return fillingStationRepository.getAllFillingStation(latitude, longitude, radius, limitInRadius, limit, STANDARD_OFFSET)
        .getItems();
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    log.info("Fetching nearest filling stations with coordinates x = {}, y = {} and radius = {} (meters)", latitude, longitude,
        radius);

    updateTimeStamp();
    return fillingStationRepository.getNearestFillingStation(latitude, longitude, radius);
  }

  @Override
  public List<FillingStation> getBestFuelPrice(double latitude, double longitude, double radius, int limitInRadius, int limit,
      String fuelType) {
    log.info("Fetching list of filling stations with coordinates x = {}, y = {}, radius = {} (meters), limitInRadius = {} "
            + "and pageLimit = {} with best fuel price for {}",
        latitude, longitude, radius, limitInRadius, limit, fuelType);

    updateTimeStamp();
    return fillingStationRepository.getBestFuelPriceStation(latitude, longitude, radius, limitInRadius, limit, STANDARD_OFFSET,
        fuelType).getItems();
  }

  @Override
  public FuelType getSupportedFuelTypes() {
    log.info("Fetch list of supported fuel types");

    return fillingStationRepository.getSupportedFuelTypes();
  }

  private void updateTimeStamp() {
    log.info("Update last modification timestamp");

    FillingStation.timestamp = fillingStationRepository.getUpdateTimestamp();
  }
}
