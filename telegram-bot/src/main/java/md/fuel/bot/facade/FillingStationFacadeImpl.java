package md.fuel.bot.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillingStationFacadeImpl implements FillingStationFacade {

  private static final int STANDARD_OFFSET = 0;

  private final FillingStationRepository fillingStationRepository;

  @Override
  public List<FillingStation> getAllFillingStations(double latitude, double longitude, double radius, int limitInRadius, int limit) {
    updateTimeStamp();
    return fillingStationRepository.getAllFillingStation(latitude, longitude, radius, limitInRadius, limit, STANDARD_OFFSET)
        .getItems();
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    updateTimeStamp();
    return fillingStationRepository.getNearestFillingStation(latitude, longitude, radius);
  }

  @Override
  public List<FillingStation> getBestFuelPrice(double latitude, double longitude, double radius, int limitInRadius,
      int limit, String fuelType) {
    updateTimeStamp();
    return fillingStationRepository.getBestFuelPriceStation(latitude, longitude, radius, limitInRadius, limit, STANDARD_OFFSET,
        fuelType).getItems();
  }

  @Override
  public FuelType getSupportedFuelTypes() {
    return fillingStationRepository.getSupportedFuelTypes();
  }

  private void updateTimeStamp() {
    FillingStation.timestamp = fillingStationRepository.getUpdateTimestamp();
  }
}
