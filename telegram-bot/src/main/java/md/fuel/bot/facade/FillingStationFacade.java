package md.fuel.bot.facade;

import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;

public interface FillingStationFacade {

  FillingStation getAllFillingStations(double latitude, double longitude, double radius, int offset);

  FillingStation getNearestFillingStation(double latitude, double longitude, double radius);

  FillingStation getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int offset);

  FuelType getSupportedFuelTypes();

  boolean hasNext(double latitude, double longitude, double radius, int offset);

  boolean hasNext(double latitude, double longitude, double radius, int offset, String fuelType);
}
