package md.fuel.bot.facade;

import java.util.List;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;

public interface FillingStationFacade {

  List<FillingStation> getAllFillingStations(double latitude, double longitude, double radius, int limitInRadius, int limit);

  FillingStation getNearestFillingStation(double latitude, double longitude, double radius);

  List<FillingStation> getBestFuelPrice(double latitude, double longitude, double radius, int limitInRadius, int limit,
      String fuelType);

  FuelType getSupportedFuelTypes();
}
