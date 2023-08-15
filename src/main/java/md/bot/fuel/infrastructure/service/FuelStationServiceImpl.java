package md.bot.fuel.infrastructure.service;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static md.bot.fuel.domain.FuelType.DIESEL;
import static md.bot.fuel.domain.FuelType.GAS;
import static md.bot.fuel.domain.FuelType.PETROL;
import static md.bot.fuel.infrastructure.utils.DistanceCalculator.calculateMeters;
import static md.bot.fuel.infrastructure.utils.DistanceCalculator.isWithinRadius;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.domain.FuelType;
import md.bot.fuel.infrastructure.api.AnreApi;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuelStationServiceImpl implements FuelStationService {

  private static final String ERROR_NO_FUEL_STATION_NEAR_YOU =
      "We can't find any fuel station near you. Try to extend search radius.";
  private static final String ERROR_NOT_FOUND_REASON_CODE = "NOT_FOUND";
  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "We found more than %s fuel stations near you. Try to decrease search radius.";
  private static final String ERROR_EXCEED_LIMIT_REASON_CODE = "EXCEED_LIMIT";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Fuel station near you do not have %s in stock. Try to extend search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final Double ZERO_PRICE = 0D;
  private static final double ZERO_PRICE_PRIMITIVE = 0D;
  private static final HashMap<FuelType, Function<FuelStation, Double>> FUEL_TYPE_FUNCTION_HASH_MAP = new HashMap<>();

  static {
    FUEL_TYPE_FUNCTION_HASH_MAP.put(PETROL, FuelStation::getPetrol);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(DIESEL, FuelStation::getDiesel);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(GAS, FuelStation::getGas);
  }

  private final AnreApi anreApi;

  @Override
  public List<FuelStation> getAllFuelStations(double latitude, double longitude, double radius, int limit) {
    final List<FuelStation> fuelStations = anreApi.getFuelStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius)
            && (checkCorrectPrice(s.getPetrol()) || checkCorrectPrice(s.getDiesel()) || checkCorrectPrice(s.getGas())))
        .collect(toList());

    checkLimit(fuelStations.size(), limit);
    if (fuelStations.isEmpty()) {
      throw new EntityNotFoundException(ERROR_NO_FUEL_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE);
    }
    return fuelStations;
  }

  @Override
  public FuelStation getNearestFuelStation(double latitude, double longitude, double radius) {
    return anreApi.getFuelStationsInfo().stream()
        .filter(s -> (checkCorrectPrice(s.getPetrol()) || checkCorrectPrice(s.getDiesel())
            || checkCorrectPrice(s.getGas())))
        .min(comparing(s -> calculateMeters(latitude, longitude, s.getLatitude(), s.getLongitude())))
        .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_FUEL_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE));
  }

  @Override
  public List<FuelStation> getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int limit) {
    final Function<FuelStation, Double> fuelStationFunction = getFuelType(fuelType);

    final List<FuelStation> filteredFuelStationsList = anreApi.getFuelStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius)
            && !isNull(fuelStationFunction.apply(s))
            && !Objects.equals(fuelStationFunction.apply(s), ZERO_PRICE))
        .collect(toList());

    final double minimalFuelPrice = filteredFuelStationsList.stream()
        .mapToDouble(fuelStationFunction::apply)
        .min()
        .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()),
            ERROR_NOT_FOUND_REASON_CODE));

    final List<FuelStation> fuelStations = filteredFuelStationsList.stream()
        .filter(station -> fuelStationFunction.apply(station).equals(minimalFuelPrice))
        .collect(toList());

    checkLimit(fuelStations.size(), limit);

    return fuelStations;
  }

  private Function<FuelStation, Double> getFuelType(String fuelType) {
    try {
      final Function<FuelStation, Double> priceFunction = FUEL_TYPE_FUNCTION_HASH_MAP.get(
          FuelType.valueOf(fuelType.toUpperCase()));

      if (isNull(priceFunction)) {
        throw new EntityNotFoundException(ERROR_INVALID_FUEL_TYPE, ERROR_NOT_FOUND_REASON_CODE);
      }

      return priceFunction;
    } catch (IllegalArgumentException e) {
      throw new EntityNotFoundException(ERROR_INVALID_FUEL_TYPE, ERROR_NOT_FOUND_REASON_CODE);
    }
  }

  private void checkLimit(int numberOfFuelStation, int limit) {
    if (numberOfFuelStation > limit) {
      throw new InvalidRequestException(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit), ERROR_EXCEED_LIMIT_REASON_CODE);
    }
  }

  private boolean checkCorrectPrice(Double price) {
    return !isNull(price) && price > ZERO_PRICE_PRIMITIVE;
  }
}
