package md.fuel.bot.infrastructure.repository;

import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;

public interface FillingStationRepository {

  Page<FillingStation> getAllFillingStation(double latitude, double longitude, double radius);

  FillingStation getNearestFillingStation(double latitude, double longitude, double radius);

  Page<FillingStation> getBestFuelPriceStation(double latitude, double longitude, double radius, String fuelType);

  String getUpdateTimestamp();

  FuelType getSupportedFuelTypes();
}
