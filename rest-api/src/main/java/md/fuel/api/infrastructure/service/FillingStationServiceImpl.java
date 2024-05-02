package md.fuel.api.infrastructure.service;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static md.fuel.api.domain.FuelType.DIESEL;
import static md.fuel.api.domain.FuelType.GAS;
import static md.fuel.api.domain.FuelType.PETROL;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.calculateMeters;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.isWithinRadius;
import static md.fuel.api.infrastructure.utils.MultiComparator.sort;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.domain.FuelType;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.repository.AnreApi;
import md.fuel.api.rest.request.SortingQuery;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FillingStationServiceImpl implements FillingStationService {

  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "More than %s filling stations were found. This is more than your specified limit. Decrease the search radius.";
  private static final String ERROR_EXCEED_LIMIT_REASON_CODE = "EXCEED_LIMIT";
  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "No filling stations were found in the specified radius. Change the search point or increase the radius.";
  private static final String ERROR_NOT_FOUND_REASON_CODE = "NOT_FOUND";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Filling stations within the specified radius do not have %s in stock. Increase the search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final double ZERO_PRICE_PRIMITIVE = 0D;
  private static final HashMap<FuelType, Function<FillingStation, Double>> FUEL_TYPE_FUNCTION_HASH_MAP = new HashMap<>();

  static {
    FUEL_TYPE_FUNCTION_HASH_MAP.put(PETROL, FillingStation::getPetrol);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(DIESEL, FillingStation::getDiesel);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(GAS, FillingStation::getGas);
  }

  private final AnreApi anreApi;

  @Override
  public List<FillingStation> getAllFillingStations(LimitFillingStationCriteria criteria) {
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    final double radius = criteria.getRadius();
    log.info("Fetching list of filling stations in radius ({} meters) with centre point x = {}, y = {}", radius, longitude,
        latitude);

    final List<FillingStation> fillingStations = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius)
            && checkCorrectPrice(s.getPetrol(), s.getDiesel(), s.getGas()))
        .collect(toList());

    checkLimit(fillingStations.size(), criteria.getLimitInRadius());

    if (fillingStations.isEmpty()) {
      log.info("No filling stations were found in the specified radius ({} meters) with centre point x = {}, y = {}", radius,
          longitude, latitude);

      throw new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE);
    }

    sort(fillingStations, getComparators(criteria.getSorting(), latitude, longitude));
    return fillingStations;
  }

  @Override
  public FillingStation getNearestFillingStation(BaseFillingStationCriteria criteria) {
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    log.info("Fetching nearest filling stations for coordinates x = {}, y = {}", longitude, latitude);

    return anreApi.getFillingStationsInfo().stream()
        .filter(s -> checkCorrectPrice(s.getPetrol(), s.getDiesel(), s.getGas()))
        .min(comparing(s -> calculateMeters(latitude, longitude, s.getLatitude(), s.getLongitude())))
        .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE));
  }

  @Override
  public List<FillingStation> getBestFuelPrice(LimitFillingStationCriteria criteria, String fuelType) {
    final Function<FillingStation, Double> fillingStationFunction = getFuelType(fuelType);
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    final double radius = criteria.getRadius();
    log.info(
        "Fetching list of filling stations in radius ({} meters) with centre point x = {}, y = {} with best fuel price for {}",
        radius, longitude, latitude, fuelType);

    final List<FillingStation> filterByDistanceList = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.getLatitude(), s.getLongitude(), radius) && !isNull(
            fillingStationFunction.apply(s)) && !Objects.equals(fillingStationFunction.apply(s), ZERO_PRICE_PRIMITIVE))
        .toList();

    final double minimalFuelPrice = getMinimalFuelPrice(filterByDistanceList, fillingStationFunction, fuelType);

    final List<FillingStation> filterByPriceList = filterByDistanceList.stream()
        .filter(station -> fillingStationFunction.apply(station).equals(minimalFuelPrice))
        .collect(toList());

    checkLimit(filterByPriceList.size(), criteria.getLimitInRadius());

    sort(filterByPriceList, getComparators(criteria.getSorting(), latitude, longitude));
    return filterByPriceList;
  }

  @Override
  public FuelPrice getAnrePrices() {
    log.info("Fetching official fuel prices");

    return anreApi.getAnrePrices();
  }

  private double getMinimalFuelPrice(List<FillingStation> filteredFillingStationsList,
      Function<FillingStation, Double> fillingStationFunction, String fuelType) {
    log.info("Find minimal price for fuel type - {} in the list with size {}", fuelType, filteredFillingStationsList.size());

    return filteredFillingStationsList.stream()
        .mapToDouble(fillingStationFunction::apply)
        .min()
        .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()),
            ERROR_NOT_FOUND_REASON_CODE));
  }

  private Function<FillingStation, Double> getFuelType(String fuelType) {
    log.info("Find fuel type from the request - {} in the map", fuelType);

    try {
      return FUEL_TYPE_FUNCTION_HASH_MAP.get(FuelType.valueOf(fuelType.toUpperCase()));
    } catch (IllegalArgumentException e) {
      log.info("Fuel type from the request - {} not found in the map", fuelType);

      throw new EntityNotFoundException(ERROR_INVALID_FUEL_TYPE, ERROR_NOT_FOUND_REASON_CODE);
    }
  }

  private boolean checkCorrectPrice(Double petrol, Double diesel, Double gas) {
    return (checkCorrectPrice(petrol) || checkCorrectPrice(diesel) || checkCorrectPrice(gas));
  }

  private boolean checkCorrectPrice(Double price) {
    return !isNull(price) && price > ZERO_PRICE_PRIMITIVE;
  }

  private List<Comparator<FillingStation>> getComparators(List<SortingQuery> sortingQuery, double latitude, double longitude) {
    log.info("Find comparator for the request {}", sortingQuery.toString());

    return sortingQuery.stream()
        .map(query -> FillingStation.getComparator(query, latitude, longitude))
        .collect(toList());
  }

  private void checkLimit(int numberOfFillingStations, int limit) {
    if (numberOfFillingStations > limit) {
      log.info("The number of filling stations found ({}) exceeds the limit ({})", numberOfFillingStations, limit);

      throw new InvalidRequestException(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit), ERROR_EXCEED_LIMIT_REASON_CODE);
    }
  }
}
