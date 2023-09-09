package md.fuel.api.infrastructure.service;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static md.fuel.api.domain.FuelType.DIESEL;
import static md.fuel.api.domain.FuelType.GAS;
import static md.fuel.api.domain.FuelType.PETROL;
import static md.fuel.api.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.calculateMeters;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.isWithinRadius;
import static md.fuel.api.infrastructure.utils.MultiComparator.sort;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.domain.FuelType;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.repository.AnreApi;
import md.fuel.api.rest.request.SortingQuery;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FillingStationServiceImpl implements FillingStationService {

  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "No filling stations were found in the specified radius. Change the search point or increase the radius.";
  private static final String ERROR_NOT_FOUND_REASON_CODE = "NOT_FOUND";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Filling stations within the specified radius do not have %s in stock. Increase the search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";
  private static final String ERROR_CAN_NOT_READ_CACHE = "Can't read value from ANRE cache";
  private static final String ERROR_CAN_NOT_READ_CACHE_REASON_CODE = "CAN_NOT_READ_CACHE";

  private static final double ZERO_PRICE_PRIMITIVE = 0D;
  private static final HashMap<FuelType, Function<FillingStation, Double>> FUEL_TYPE_FUNCTION_HASH_MAP = new HashMap<>();

  static {
    FUEL_TYPE_FUNCTION_HASH_MAP.put(PETROL, FillingStation::petrol);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(DIESEL, FillingStation::diesel);
    FUEL_TYPE_FUNCTION_HASH_MAP.put(GAS, FillingStation::gas);
  }

  private final AnreApi anreApi;
  private final CacheManager cacheManager;

  @Override
  public List<FillingStation> getAllFillingStations(LimitFillingStationCriteria criteria) {
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    final double radius = criteria.getRadius();

    final List<FillingStation> fillingStations = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.latitude(), s.longitude(), radius)
            && checkCorrectPrice(s.petrol(), s.diesel(), s.gas()))
        .collect(toList());

    if (fillingStations.isEmpty()) {
      throw new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE);
    }

    sort(fillingStations, getComparators(criteria.getSorting(), latitude, longitude));
    return filterByOffsetAndLimit(fillingStations, criteria.getPageLimit(), criteria.getPageOffset());
  }

  @Override
  public FillingStation getNearestFillingStation(BaseFillingStationCriteria criteria) {
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();

    return anreApi.getFillingStationsInfo().stream()
        .filter(s -> checkCorrectPrice(s.petrol(), s.diesel(), s.gas()))
        .min(comparing(s -> calculateMeters(latitude, longitude, s.latitude(), s.longitude())))
        .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE));
  }

  @Override
  public List<FillingStation> getBestFuelPrice(LimitFillingStationCriteria criteria, String fuelType) {
    final Function<FillingStation, Double> fillingStationFunction = getFuelType(fuelType);
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    final double radius = criteria.getRadius();

    final List<FillingStation> filterByDistanceList = anreApi.getFillingStationsInfo().stream()
        .filter(s -> isWithinRadius(latitude, longitude, s.latitude(), s.longitude(), radius) && !isNull(
            fillingStationFunction.apply(s)) && !Objects.equals(fillingStationFunction.apply(s), ZERO_PRICE_PRIMITIVE))
        .toList();

    final double minimalFuelPrice = getMinimalFuelPrice(filterByDistanceList, fillingStationFunction, fuelType);

    final List<FillingStation> filterByPriceList = filterByDistanceList.stream()
        .filter(station -> fillingStationFunction.apply(station).equals(minimalFuelPrice))
        .collect(toList());

    sort(filterByPriceList, getComparators(criteria.getSorting(), latitude, longitude));
    return filterByOffsetAndLimit(filterByPriceList, criteria.getPageLimit(), criteria.getPageOffset());
  }

  @Override
  public FuelPrice getAnrePrices() {
    return anreApi.getAnrePrices();
  }

  @Override
  public int getTotalNumberOfFillingStations() {
    try {
      if (cacheManager.getCache(ANRE_CACHE).iterator().next().getValue() instanceof List<?> list) {
        return list.size();
      }
    } catch (RuntimeException exception) {
      throw new InfrastructureException(ERROR_CAN_NOT_READ_CACHE, ERROR_CAN_NOT_READ_CACHE_REASON_CODE);
    }
    throw new InfrastructureException(ERROR_CAN_NOT_READ_CACHE, ERROR_CAN_NOT_READ_CACHE_REASON_CODE);
  }

  private double getMinimalFuelPrice(List<FillingStation> filteredFillingStationsList,
      Function<FillingStation, Double> fillingStationFunction, String fuelType) {
    return filteredFillingStationsList.stream()
        .mapToDouble(fillingStationFunction::apply)
        .min()
        .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()),
            ERROR_NOT_FOUND_REASON_CODE));
  }

  private Function<FillingStation, Double> getFuelType(String fuelType) {
    try {
      return FUEL_TYPE_FUNCTION_HASH_MAP.get(FuelType.valueOf(fuelType.toUpperCase()));
    } catch (IllegalArgumentException e) {
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
    return sortingQuery.stream()
        .map(query -> FillingStation.getComparator(query, latitude, longitude))
        .collect(toList());
  }

  private List<FillingStation> filterByOffsetAndLimit(List<FillingStation> fillingStations, Integer limit, Integer offset) {
    if (isNull(limit) || isNull(offset)) {
      return fillingStations;
    }
    return fillingStations.stream()
        .skip(offset)
        .limit(limit)
        .toList();
  }
}
