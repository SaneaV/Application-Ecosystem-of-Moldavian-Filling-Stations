package md.bot.fuel.facade;

import java.util.List;
import md.bot.fuel.facade.dto.FuelStationDto;

public interface FuelStationFacade {

    List<FuelStationDto> getAllFuelStations(double userLatitude, double userLongitude, double radius, int limit);

    FuelStationDto getNearestFuelStation(double userLatitude, double userLongitude, double radius);

    List<FuelStationDto> getBestFuelPrice(double userLatitude, double userLongitude, double radius, String fuelType, int limit);
}
