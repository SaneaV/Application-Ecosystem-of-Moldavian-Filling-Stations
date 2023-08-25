package md.fuel.api.infrastructure.service;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static md.fuel.api.domain.FuelType.DIESEL;
import static md.fuel.api.domain.FuelType.GAS;
import static md.fuel.api.domain.FuelType.PETROL;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.calculateMeters;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.isWithinRadius;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelType;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.repository.AnreApi;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FillingStationServiceImpl implements FillingStationService {

  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "We can't find any filling station near you. Try to extend search radius.";
  private static final String ERROR_NOT_FOUND_REASON_CODE = "NOT_FOUND";
  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "We found more than %s filling stations near you. Try to decrease search radius.";
  private static final String ERROR_EXCEED_LIMIT_REASON_CODE = "EXCEED_LIMIT";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Filling station near you do not have %s in stock. Try to extend search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final Double ZERO_PRICE = 0D;
  private static final double ZERO_PRICE_PRIMITIVE = 0D;
  private static final HashMap<FuelType, Function<FillingStation, Double>> FUEL_TYPE_FUNCTION_HASH_MAP = new HashMap<>();

  static {
    FUEL_TYPE_FUNCTION_HASH_MAP.put(PETROL, FillingStation::getPetrol);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(DIESEL, FillingStation::getDiesel);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(GAS, FillingStation::getGas);
  }

  private final AnreApi anreApi;

  @Override
  public List<FillingStation> getAllFillingStations(double latitude, double longitude, double radius, int limit) {
    final List<FillingStation> fillingStations = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius)
            && (checkCorrectPrice(s.getPetrol()) || checkCorrectPrice(s.getDiesel()) || checkCorrectPrice(s.getGas())))
        .sorted(getDistanceComparator(latitude, longitude))
        .collect(toList());

    checkLimit(fillingStations.size(), limit);
    if (fillingStations.isEmpty()) {
      throw new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE);
    }
    return fillingStations;
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    return anreApi.getFillingStationsInfo().stream()
        .filter(s -> (checkCorrectPrice(s.getPetrol()) || checkCorrectPrice(s.getDiesel())
            || checkCorrectPrice(s.getGas())))
        .min(comparing(s -> calculateMeters(latitude, longitude, s.getLatitude(), s.getLongitude())))
        .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE));
  }

  @Override
  public List<FillingStation> getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int limit) {
    final Function<FillingStation, Double> fillingStationFunction = getFuelType(fuelType);

    final List<FillingStation> filteredFillingStationsList = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius)
            && !isNull(fillingStationFunction.apply(s))
            && !Objects.equals(fillingStationFunction.apply(s), ZERO_PRICE))
        .collect(toList());

    final double minimalFuelPrice = filteredFillingStationsList.stream()
        .mapToDouble(fillingStationFunction::apply)
        .min()
        .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()),
            ERROR_NOT_FOUND_REASON_CODE));

    final List<FillingStation> fillingStations = filteredFillingStationsList.stream()
        .filter(station -> fillingStationFunction.apply(station).equals(minimalFuelPrice))
        .sorted(getDistanceComparator(latitude, longitude))
        .collect(toList());

    checkLimit(fillingStations.size(), limit);

    return fillingStations;
  }

  private Function<FillingStation, Double> getFuelType(String fuelType) {
    try {
      return FUEL_TYPE_FUNCTION_HASH_MAP.get(FuelType.valueOf(fuelType.toUpperCase()));
    } catch (IllegalArgumentException e) {
      throw new EntityNotFoundException(ERROR_INVALID_FUEL_TYPE, ERROR_NOT_FOUND_REASON_CODE);
    }
  }

  private void checkLimit(int numberOfFillingStations, int limit) {
    if (numberOfFillingStations > limit) {
      throw new InvalidRequestException(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit), ERROR_EXCEED_LIMIT_REASON_CODE);
    }
  }

  private boolean checkCorrectPrice(Double price) {
    return !isNull(price) && price > ZERO_PRICE_PRIMITIVE;
  }

  private Comparator<FillingStation> getDistanceComparator(double latitude, double longitude) {
    return Comparator.comparingDouble(s -> calculateMeters(latitude, longitude, s.getLatitude(), s.getLongitude()));
  }
}
