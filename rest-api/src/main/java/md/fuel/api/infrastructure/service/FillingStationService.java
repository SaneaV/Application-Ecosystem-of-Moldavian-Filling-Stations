package md.fuel.api.infrastructure.service;

import java.util.List;
import md.fuel.api.domain.FillingStation;

public interface FillingStationService {

  List<FillingStation> getAllFillingStations(double userLatitude, double userLongitude, double radius, int limit);

  FillingStation getNearestFillingStation(double userLatitude, double userLongitude, double radius);

  List<FillingStation> getBestFuelPrice(double userLatitude, double userLongitude, double radius, String fuelType, int limit);
}
