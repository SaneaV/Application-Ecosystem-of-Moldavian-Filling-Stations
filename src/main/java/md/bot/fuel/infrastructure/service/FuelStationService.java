package md.bot.fuel.infrastructure.service;

import java.util.List;
import md.bot.fuel.domain.FuelStation;

public interface FuelStationService {

  List<FuelStation> getAllFuelStations(double userLatitude, double userLongitude, double radius, int limit);

  FuelStation getNearestFuelStation(double userLatitude, double userLongitude, double radius);

  List<FuelStation> getBestFuelPrice(double userLatitude, double userLongitude, double radius, String fuelType, int limit);
}
