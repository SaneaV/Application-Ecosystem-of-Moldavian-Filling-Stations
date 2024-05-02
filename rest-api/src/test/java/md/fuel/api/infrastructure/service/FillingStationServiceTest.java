package md.fuel.api.infrastructure.service;

import static java.util.Arrays.asList;
import static md.fuel.api.rest.request.SortOrder.ASC;
import static md.fuel.api.rest.request.SortOrder.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.cache.CacheManager;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.repository.AnreApi;
import md.fuel.api.rest.request.SortOrder;
import md.fuel.api.rest.request.SortingQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FillingStationServiceTest {

  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "More than %s filling stations were found. This is more than your specified limit. Decrease the search radius.";
  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "No filling stations were found in the specified radius. Change the search point or increase the radius.";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Filling stations within the specified radius do not have %s in stock. Increase the search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final String FILLING_STATION_NAME = "Filling station";
  private static final String FILLING_STATION_IN_ANOTHER_LOCATION = "Filling Station In another Location";
  private static final String FILLING_STATION_WITH_BEST_FUEL_PRICE_1 = "Best fuel price station - 1";
  private static final String FILLING_STATION_WITH_BEST_FUEL_PRICE_2 = "Best fuel price station - 2";
  private static final Double DEFAULT_PRICE = 10.0;

  private static final FillingStation BEST_PETROL_FILLING_STATION = new FillingStation("bestPetrolFillingStation", 1.0,
      DEFAULT_PRICE, DEFAULT_PRICE, 3, 3);
  private static final FillingStation BEST_DIESEL_FILLING_STATION = new FillingStation("bestDieselFillingStation", DEFAULT_PRICE,
      1.0, DEFAULT_PRICE, 3, 3);
  private static final FillingStation BEST_GAS_FILLING_STATION = new FillingStation("bestGasFillingStation", DEFAULT_PRICE,
      DEFAULT_PRICE, 1.0, 3, 3);
  private static final FillingStation NEAREST_FILLING_STATION = new FillingStation("nearestFillingStation", DEFAULT_PRICE,
      DEFAULT_PRICE, DEFAULT_PRICE, 3.000003, 3.000003);
  private static final FillingStation FIRST_NAME_FILLING_STATION = new FillingStation("AFirstNameFillingStation", DEFAULT_PRICE,
      DEFAULT_PRICE, DEFAULT_PRICE, 3, 3);
  private static final List<FillingStation> FILLING_STATIONS = asList(BEST_PETROL_FILLING_STATION, BEST_DIESEL_FILLING_STATION,
      BEST_GAS_FILLING_STATION, NEAREST_FILLING_STATION, FIRST_NAME_FILLING_STATION);

  private final AnreApi anreApi;
  private final FillingStationService fillingStationService;
  private final CacheManager cacheManager = mock(CacheManager.class);

  public FillingStationServiceTest() {
    this.anreApi = mock(AnreApi.class);
    this.fillingStationService = new FillingStationServiceImpl(anreApi);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getAllFillingStations if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetAllFillingStations() {
    final LimitFillingStationCriteria criteria = buildLimitCriteria(10, 10, 30, new ArrayList<>());
    assertThatThrownBy(() -> fillingStationService.getAllFillingStations(criteria))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FILLING_STATION_NEAR_YOU);

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getNearestFillingStation if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetNearestFillingStation() {
    final LimitFillingStationCriteria criteria = buildLimitCriteria(10, 10, 30, new ArrayList<>());
    assertThatThrownBy(() -> fillingStationService.getNearestFillingStation(criteria))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FILLING_STATION_NEAR_YOU);

    verify(anreApi).getFillingStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {"Petrol", "Diesel", "Gas"})
  @DisplayName("Should throw EntityNotFoundException on getBestFuelPrice if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetBestFuelPrice(String fuelType) {
    final List<FillingStation> fillingStations = getFillingStations();
    final LimitFillingStationCriteria criteria = buildLimitCriteria(0, 0, 30, new ArrayList<>());

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationService.getBestFuelPrice(criteria, fuelType))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()));

    verify(anreApi).getFillingStationsInfo();
  }

  @ParameterizedTest
  @CsvSource(value = {"10, null", "null, null"}, nullValues = "null")
  @DisplayName("Should return all filling stations in radius")
  void shouldReturnAllFillingStationsInRadius(Integer limit, Integer offset) {
    final List<FillingStation> fillingStations = getFillingStations();
    final LimitFillingStationCriteria criteria = new LimitFillingStationCriteria(3.006, 3.006, 500,
        50, new ArrayList<>(), limit, offset);

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final List<FillingStation> allFillingStations = fillingStationService.getAllFillingStations(criteria);
    assertThat(allFillingStations.size()).isEqualTo(10);

    final List<String> fillingStationsName = allFillingStations.stream()
        .map(FillingStation::getName)
        .toList();
    assertThat(fillingStationsName).doesNotContain(FILLING_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if number of results greater than limit on getAllFillingStations")
  void shouldThrowInvalidRequestExceptionOnGetAllFillingStationsOnGreaterLimit() {
    final int limit = 5;
    final List<FillingStation> fillingStations = getFillingStations();
    final LimitFillingStationCriteria criteria = new LimitFillingStationCriteria(3.006, 3.006, 500,
        5, new ArrayList<>(), 10, 0);

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationService.getAllFillingStations(criteria))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should return all filling stations in radius with limit and offset")
  void shouldReturnAllFillingStationsInRadiusWithLimitAndOffset() {
    final List<FillingStation> fillingStations = getFillingStations();
    final LimitFillingStationCriteria criteria = new LimitFillingStationCriteria(3.006, 3.006, 500,
        50, new ArrayList<>(), 8, 2);

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final List<FillingStation> allFillingStations = fillingStationService.getAllFillingStations(criteria);
    assertThat(allFillingStations).hasSize(fillingStations.size() - criteria.getPageOffset());

    final List<String> fillingStationsName = allFillingStations.stream()
        .map(FillingStation::getName)
        .toList();
    assertThat(fillingStationsName).doesNotContain(FILLING_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should return nearest filling station in radius")
  void shouldReturnNearestFillingStationInRadius() {
    final List<FillingStation> fillingStations = getFillingStations();
    final BaseFillingStationCriteria criteria = new BaseFillingStationCriteria(3, 3, 1000);

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final FillingStation nearestFillingStation = fillingStationService.getNearestFillingStation(criteria);
    assertThat(nearestFillingStation.getName()).isEqualTo(FILLING_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFillingStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {"Petrol", "Diesel", "Gas"})
  @DisplayName("Should return filling station with the best fuel price")
  void shouldReturnBestFuelPriceStation(String fuelType) {
    final List<FillingStation> fillingStations = getFillingStations();
    final LimitFillingStationCriteria criteria = buildLimitCriteria(3.006, 3.006, 1000000000, new ArrayList<>());

    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final List<FillingStation> bestFuelPrice = fillingStationService.getBestFuelPrice(criteria, fuelType);

    final List<String> fillingStationsName = bestFuelPrice.stream()
        .map(FillingStation::getName)
        .toList();

    assertThat(bestFuelPrice.size()).isEqualTo(2);
    assertThat(fillingStationsName).containsAll(asList(FILLING_STATION_WITH_BEST_FUEL_PRICE_1,
        FILLING_STATION_WITH_BEST_FUEL_PRICE_2));

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on invalid fuelType")
  void shouldThrowEntityNotFoundExceptionOnInvalidFuelType() {
    final LimitFillingStationCriteria criteria = buildLimitCriteria(10, 10, 30, new ArrayList<>());

    assertThatThrownBy(() -> fillingStationService.getBestFuelPrice(criteria, "Invalid Fuel Type"))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_INVALID_FUEL_TYPE);

    verifyNoInteractions(anreApi);
  }

  @ParameterizedTest
  @MethodSource("getComparatorParameters")
  @DisplayName("Should compare by specified comparator")
  void shouldCompareBySpecificComparator(String sortBy, SortOrder sortOrder, int index, String stationName) {
    final ArrayList<SortingQuery> sortingQueries = new ArrayList<>();
    sortingQueries.add(new SortingQuery(sortBy, sortOrder));
    final LimitFillingStationCriteria criteria = buildLimitCriteria(3.000004, 3.000004, 100, sortingQueries);

    when(anreApi.getFillingStationsInfo()).thenReturn(shuffledList());

    final List<FillingStation> ascResult = fillingStationService.getAllFillingStations(criteria);

    verify(anreApi).getFillingStationsInfo();

    assertThat(ascResult).hasSize(5);
    assertThat(ascResult.get(index).getName()).isEqualTo(stationName);
  }

  @Test
  @DisplayName("Should compare by multiple comparators in ascending order")
  void shouldCompareByMultipleComparatorsInAscendingOrder() {
    final ArrayList<SortingQuery> sortingQueries = new ArrayList<>();
    sortingQueries.add(new SortingQuery("distance", ASC));
    sortingQueries.add(new SortingQuery("name", ASC));
    sortingQueries.add(new SortingQuery("diesel", ASC));
    sortingQueries.add(new SortingQuery("gas", ASC));
    sortingQueries.add(new SortingQuery("petrol", ASC));
    final LimitFillingStationCriteria criteria = buildLimitCriteria(3.000004, 3.000004, 100, sortingQueries);

    when(anreApi.getFillingStationsInfo()).thenReturn(shuffledList());

    final List<FillingStation> ascResult = fillingStationService.getAllFillingStations(criteria);

    verify(anreApi).getFillingStationsInfo();

    assertThat(ascResult).hasSize(5);
    assertThat(ascResult.get(0).getName()).isEqualTo(NEAREST_FILLING_STATION.getName());
    assertThat(ascResult.get(1).getName()).isEqualTo(FIRST_NAME_FILLING_STATION.getName());
    assertThat(ascResult.get(2).getName()).isEqualTo(BEST_DIESEL_FILLING_STATION.getName());
    assertThat(ascResult.get(3).getName()).isEqualTo(BEST_GAS_FILLING_STATION.getName());
    assertThat(ascResult.get(4).getName()).isEqualTo(BEST_PETROL_FILLING_STATION.getName());
  }

  @Test
  @DisplayName("Should compare by multiple comparators in descending order")
  void shouldCompareByMultipleComparatorsInDescendingOrder() {
    final ArrayList<SortingQuery> sortingQueries = new ArrayList<>();
    sortingQueries.add(new SortingQuery("distance", DESC));
    sortingQueries.add(new SortingQuery("name", DESC));
    sortingQueries.add(new SortingQuery("diesel", DESC));
    sortingQueries.add(new SortingQuery("gas", DESC));
    sortingQueries.add(new SortingQuery("petrol", DESC));
    final LimitFillingStationCriteria criteria = buildLimitCriteria(3.000004, 3.000004, 100, sortingQueries);

    when(anreApi.getFillingStationsInfo()).thenReturn(shuffledList());

    final List<FillingStation> ascResult = fillingStationService.getAllFillingStations(criteria);

    verify(anreApi).getFillingStationsInfo();

    assertThat(ascResult).hasSize(5);
    assertThat(ascResult.get(4).getName()).isEqualTo(NEAREST_FILLING_STATION.getName());
    assertThat(ascResult.get(3).getName()).isEqualTo(FIRST_NAME_FILLING_STATION.getName());
    assertThat(ascResult.get(2).getName()).isEqualTo(BEST_DIESEL_FILLING_STATION.getName());
    assertThat(ascResult.get(1).getName()).isEqualTo(BEST_GAS_FILLING_STATION.getName());
    assertThat(ascResult.get(0).getName()).isEqualTo(BEST_PETROL_FILLING_STATION.getName());
  }

  @Test
  @DisplayName("Should throw exception on wrong comparator name")
  void shouldThrowExceptionOnWrongComparatorName() {
    final ArrayList<SortingQuery> sortingQueries = new ArrayList<>();
    sortingQueries.add(new SortingQuery("WRONG-COMPARATOR-NAME", DESC));
    final LimitFillingStationCriteria criteria = buildLimitCriteria(3.000004, 3.000004, 100, sortingQueries);

    when(anreApi.getFillingStationsInfo()).thenReturn(shuffledList());

    assertThatThrownBy(() -> fillingStationService.getAllFillingStations(criteria))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage("Wrong sorting parameter: WRONG-COMPARATOR-NAME");

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should return ANRE prices")
  void shouldReturnAnrePrices() {
    final FuelPrice fuelPrice = new FuelPrice(DEFAULT_PRICE, DEFAULT_PRICE, LOCAL_DATE.toString());

    when(anreApi.getAnrePrices()).thenReturn(fuelPrice);

    final FuelPrice anrePrices = fillingStationService.getAnrePrices();

    verify(anreApi).getAnrePrices();

    assertThat(anrePrices).isEqualTo(fuelPrice);
  }

  private static Stream<Arguments> getComparatorParameters() {
    return Stream.of(
        Arguments.of("name", ASC, 0, FIRST_NAME_FILLING_STATION.getName()),
        Arguments.of("name", null, 0, FIRST_NAME_FILLING_STATION.getName()),
        Arguments.of("name", DESC, 4, FIRST_NAME_FILLING_STATION.getName()),
        Arguments.of("petrol", ASC, 0, BEST_PETROL_FILLING_STATION.getName()),
        Arguments.of("petrol", null, 0, BEST_PETROL_FILLING_STATION.getName()),
        Arguments.of("petrol", DESC, 4, BEST_PETROL_FILLING_STATION.getName()),
        Arguments.of("diesel", ASC, 0, BEST_DIESEL_FILLING_STATION.getName()),
        Arguments.of("diesel", null, 0, BEST_DIESEL_FILLING_STATION.getName()),
        Arguments.of("diesel", DESC, 4, BEST_DIESEL_FILLING_STATION.getName()),
        Arguments.of("gas", ASC, 0, BEST_GAS_FILLING_STATION.getName()),
        Arguments.of("gas", null, 0, BEST_GAS_FILLING_STATION.getName()),
        Arguments.of("gas", DESC, 4, BEST_GAS_FILLING_STATION.getName()),
        Arguments.of("distance", ASC, 0, NEAREST_FILLING_STATION.getName()),
        Arguments.of("distance", null, 0, NEAREST_FILLING_STATION.getName()),
        Arguments.of("distance", DESC, 4, NEAREST_FILLING_STATION.getName())
    );
  }

  private List<FillingStation> getFillingStations() {
    final FillingStation fillingStation = new FillingStation(FILLING_STATION_NAME, 10.0, 10.0, 10.0, 3.005, 3.005);
    final FillingStation fillingStation1 = new FillingStation(FILLING_STATION_WITH_BEST_FUEL_PRICE_1, 5.0, 6.0, 7.0, 3.005,
        3.005);
    final FillingStation fillingStation2 = new FillingStation(FILLING_STATION_WITH_BEST_FUEL_PRICE_2, 5.0, 6.0, 7.0, 3.005,
        3.005);
    final FillingStation fillingStation3 = new FillingStation(FILLING_STATION_NAME, 12.0, 30.0, 102.0, 3.005, 3.005);
    final FillingStation fillingStation4 = new FillingStation(FILLING_STATION_NAME, null, 40.0, 103.0, 3.005, 3.005);
    final FillingStation fillingStation5 = new FillingStation(FILLING_STATION_NAME, 14.0, null, 104.0, 3.005, 3.005);
    final FillingStation fillingStation6 = new FillingStation(FILLING_STATION_NAME, 15.0, 60.0, null, 3.005, 3.005);
    final FillingStation fillingStation7 = new FillingStation(FILLING_STATION_NAME, 0D, 70.0, 106.0, 3.005, 3.005);
    final FillingStation fillingStation8 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 107.0, 3.005, 3.005);
    final FillingStation fillingStation9 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 10.0, 3.005, 3.005);
    final FillingStation fillingStation10 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 0D, 3.005, 3.005);
    final FillingStation fillingStation11 = new FillingStation(FILLING_STATION_IN_ANOTHER_LOCATION, 19.0, 100.0, 109.0, 3.000001,
        3.000001);
    return asList(fillingStation, fillingStation1, fillingStation2, fillingStation3, fillingStation4, fillingStation5,
        fillingStation6, fillingStation7, fillingStation8, fillingStation9, fillingStation10, fillingStation11);
  }

  private List<FillingStation> shuffledList() {
    Collections.shuffle(FILLING_STATIONS);
    return FILLING_STATIONS;
  }

  private LimitFillingStationCriteria buildLimitCriteria(double latitude, double longitude, double radius,
      List<SortingQuery> sortingQueries) {
    return new LimitFillingStationCriteria(latitude, longitude, radius, 50, sortingQueries, null, null);
  }
}
