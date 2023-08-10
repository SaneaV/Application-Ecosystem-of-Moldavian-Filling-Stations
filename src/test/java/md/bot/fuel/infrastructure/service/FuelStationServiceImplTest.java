package md.bot.fuel.infrastructure.service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.infrastructure.api.AnreApi;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FuelStationServiceImplTest {

  private static final String ERROR_NO_FUEL_STATION_NEAR_YOU = "We can't find any fuel station near you. Try to extend search radius.";
  private static final String ERROR_FOUND_MORE_THAN_LIMIT = "We found more than %s fuel stations near you. Try to decrease search radius.";
  private static final String ERROR_NO_FUEL_IN_STOCK = "Fuel station near you do not have %s in stock. Try to extend search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final String FUEL_STATION_NAME = "Fuel station";
  private static final String FUEL_STATION_IN_ANOTHER_LOCATION = "Fuel Station In another Location";
  private static final String FUEL_STATION_WITH_BEST_FUEL_PRICE_1 = "Best fuel price station - 1";
  private static final String FUEL_STATION_WITH_BEST_FUEL_PRICE_2 = "Best fuel price station - 2";
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int LIMIT = 50;
  private static final String PETROL = "Petrol";
  private static final String DIESEL = "Diesel";
  private static final String GAS = "Gas";

  private final AnreApi anreApi;
  private final FuelStationService fuelStationService;

  public FuelStationServiceImplTest() {
    this.anreApi = mock(AnreApi.class);
    this.fuelStationService = new FuelStationServiceImpl(anreApi);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getAllFuelStations if no fuel station found")
  void shouldThrowEntityNotFoundExceptionOnGetAllFuelStations() {
    assertThatThrownBy(() -> fuelStationService.getAllFuelStations(LATITUDE, LONGITUDE, RADIUS, LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FUEL_STATION_NEAR_YOU);

    verify(anreApi).getFuelStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getNearestFuelStation if no fuel station found")
  void shouldThrowEntityNotFoundExceptionOnGetNearestFuelStation() {
    assertThatThrownBy(() -> fuelStationService.getNearestFuelStation(LATITUDE, LONGITUDE, RADIUS))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FUEL_STATION_NEAR_YOU);

    verify(anreApi).getFuelStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {PETROL, DIESEL, GAS})
  @DisplayName("Should throw EntityNotFoundException on getBestFuelPrice if no fuel station found")
  void shouldThrowEntityNotFoundExceptionOnGetBestFuelPrice(String fuelType) {
    final List<FuelStation> fuelStations = getFuelStations();
    when(anreApi.getFuelStationsInfo()).thenReturn(fuelStations);

    assertThatThrownBy(() -> fuelStationService.getBestFuelPrice(0, 0, RADIUS, fuelType, LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()));

    verify(anreApi).getFuelStationsInfo();
  }

  @Test
  @DisplayName("Should return all fuel stations in radius")
  void shouldReturnAllFuelStationsInRadius() {
    final List<FuelStation> fuelStations = getFuelStations();
    when(anreApi.getFuelStationsInfo()).thenReturn(fuelStations);

    final List<FuelStation> allFuelStations = fuelStationService.getAllFuelStations(47.002883, 28.867443, 7000, LIMIT);
    assertThat(allFuelStations.size()).isEqualTo(10);

    final List<String> fuelStationsName = allFuelStations.stream()
        .map(FuelStation::getName)
        .collect(toList());
    assertThat(fuelStationsName).doesNotContain(FUEL_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFuelStationsInfo();
  }

  @Test
  @DisplayName("Should return nearest fuel station in radius")
  void shouldReturnNearestFuelStationInRadius() {
    final List<FuelStation> fuelStations = getFuelStations();
    when(anreApi.getFuelStationsInfo()).thenReturn(fuelStations);

    final FuelStation nearestFuelStation = fuelStationService.getNearestFuelStation(46.970797, 28.727367, 7000);
    assertThat(nearestFuelStation.getName()).isEqualTo(FUEL_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFuelStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {PETROL, DIESEL, GAS})
  @DisplayName("Should return fuel station with the best fuel price")
  void shouldReturnBestFuelPriceStation(String fuelType) {
    final List<FuelStation> fuelStations = getFuelStations();
    when(anreApi.getFuelStationsInfo()).thenReturn(fuelStations);

    final List<FuelStation> bestFuelPrice = fuelStationService.getBestFuelPrice(46.970797, 28.727367, 100000, fuelType, LIMIT);

    final List<String> fuelStationsName = bestFuelPrice.stream()
        .map(FuelStation::getName)
        .collect(toList());
    assertThat(bestFuelPrice.size()).isEqualTo(2);
    assertThat(fuelStationsName).containsAll(asList(FUEL_STATION_WITH_BEST_FUEL_PRICE_1, FUEL_STATION_WITH_BEST_FUEL_PRICE_2));

    verify(anreApi).getFuelStationsInfo();
  }

  @Test
  @DisplayName("Should throw InvalidRequestException on getAllFuelStations if found more than 10 fuel stations")
  void shouldThrowInvalidRequestExceptionOnAllFuelStationsInRadius() {
    final int limit = 10;
    final List<FuelStation> fuelStations = getFuelStations();
    when(anreApi.getFuelStationsInfo()).thenReturn(fuelStations);

    assertThatThrownBy(() -> fuelStationService.getAllFuelStations(47.018334, 28.860920, 1500000, limit))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));

    verify(anreApi).getFuelStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on invalid fuelType")
  void shouldThrowEntityNotFoundExceptionOnInvalidFuelType() {
    assertThatThrownBy(() -> fuelStationService.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, "Invalid Fuel Type", LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_INVALID_FUEL_TYPE);

    verifyNoInteractions(anreApi);
  }

  private List<FuelStation> getFuelStations() {
    final FuelStation fuelStation = new FuelStation(FUEL_STATION_NAME, 10.0, 10.0, 10.0, 46.976887, 28.881841);
    final FuelStation fuelStation1 = new FuelStation(FUEL_STATION_WITH_BEST_FUEL_PRICE_1, 5.0, 6.0, 7.0, 47.027295, 28.850302);
    final FuelStation fuelStation2 = new FuelStation(FUEL_STATION_WITH_BEST_FUEL_PRICE_2, 5.0, 6.0, 7.0, 47.022135, 28.825429);
    final FuelStation fuelStation3 = new FuelStation(FUEL_STATION_NAME, 12.0, 30.0, 102.0, 47.030981, 28.874094);
    final FuelStation fuelStation4 = new FuelStation(FUEL_STATION_NAME, null, 40.0, 103.0, 46.992269, 28.885449);
    final FuelStation fuelStation5 = new FuelStation(FUEL_STATION_NAME, 14.0, null, 104.0, 46.996093, 28.845470);
    final FuelStation fuelStation6 = new FuelStation(FUEL_STATION_NAME, 15.0, 60.0, null, 47.013886, 28.801868);
    final FuelStation fuelStation7 = new FuelStation(FUEL_STATION_NAME, 0D, 70.0, 106.0, 47.008970, 28.805302);
    final FuelStation fuelStation8 = new FuelStation(FUEL_STATION_NAME, 0D, 0D, 107.0, 47.021611, 28.904865);
    final FuelStation fuelStation9 = new FuelStation(FUEL_STATION_NAME, 0D, 0D, 10.0, 47.048286, 28.819721);
    final FuelStation fuelStation10 = new FuelStation(FUEL_STATION_NAME, 0D, 0D, 0D, 47.000308, 28.856800);
    final FuelStation fuelStation11 = new FuelStation(FUEL_STATION_IN_ANOTHER_LOCATION, 19.0, 100.0, 109.0, 46.975248, 28.744190);
    return asList(fuelStation, fuelStation1, fuelStation2, fuelStation3, fuelStation4, fuelStation5, fuelStation6, fuelStation7,
        fuelStation8, fuelStation9, fuelStation10, fuelStation11);
  }
}
