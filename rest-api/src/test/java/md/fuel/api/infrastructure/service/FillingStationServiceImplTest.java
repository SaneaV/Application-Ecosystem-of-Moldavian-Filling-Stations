package md.fuel.api.infrastructure.service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.repository.AnreApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FillingStationServiceImplTest {

  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "We can't find any filling station near you. Try to extend search radius.";
  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "We found more than %s filling stations near you. Try to decrease search radius.";
  private static final String ERROR_NO_FUEL_IN_STOCK =
      "Filling station near you do not have %s in stock. Try to extend search radius.";
  private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

  private static final String FILLING_STATION_NAME = "Filling station";
  private static final String FILLING_STATION_IN_ANOTHER_LOCATION = "Filling Station In another Location";
  private static final String FILLING_STATION_WITH_BEST_FUEL_PRICE_1 = "Best fuel price station - 1";
  private static final String FILLING_STATION_WITH_BEST_FUEL_PRICE_2 = "Best fuel price station - 2";
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int LIMIT = 50;
  private static final String PETROL = "Petrol";
  private static final String DIESEL = "Diesel";
  private static final String GAS = "Gas";

  private final AnreApi anreApi;
  private final FillingStationService fillingStationService;

  public FillingStationServiceImplTest() {
    this.anreApi = mock(AnreApi.class);
    this.fillingStationService = new FillingStationServiceImpl(anreApi);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getAllFillingStations if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetAllFillingStations() {
    assertThatThrownBy(() -> fillingStationService.getAllFillingStations(LATITUDE, LONGITUDE, RADIUS, LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FILLING_STATION_NEAR_YOU);

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on getNearestFillingStation if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetNearestFillingStation() {
    assertThatThrownBy(() -> fillingStationService.getNearestFillingStation(LATITUDE, LONGITUDE, RADIUS))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_NO_FILLING_STATION_NEAR_YOU);

    verify(anreApi).getFillingStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {PETROL, DIESEL, GAS})
  @DisplayName("Should throw EntityNotFoundException on getBestFuelPrice if no filling station found")
  void shouldThrowEntityNotFoundExceptionOnGetBestFuelPrice(String fuelType) {
    final List<FillingStation> fillingStations = getFillingStations();
    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationService.getBestFuelPrice(0, 0, RADIUS, fuelType, LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase()));

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should return all filling stations in radius")
  void shouldReturnAllFillingStationsInRadius() {
    final List<FillingStation> fillingStations = getFillingStations();
    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final List<FillingStation> allFillingStations = fillingStationService.getAllFillingStations(47.002883, 28.867443, 7000,
        LIMIT);
    assertThat(allFillingStations.size()).isEqualTo(10);

    final List<String> fillingStationsName = allFillingStations.stream()
        .map(FillingStation::getName)
        .collect(toList());
    assertThat(fillingStationsName).doesNotContain(FILLING_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should return nearest filling station in radius")
  void shouldReturnNearestFillingStationInRadius() {
    final List<FillingStation> fillingStations = getFillingStations();
    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final FillingStation nearestFillingStation = fillingStationService.getNearestFillingStation(46.970797, 28.727367, 7000);
    assertThat(nearestFillingStation.getName()).isEqualTo(FILLING_STATION_IN_ANOTHER_LOCATION);

    verify(anreApi).getFillingStationsInfo();
  }

  @ParameterizedTest
  @ValueSource(strings = {PETROL, DIESEL, GAS})
  @DisplayName("Should return filling station with the best fuel price")
  void shouldReturnBestFuelPriceStation(String fuelType) {
    final List<FillingStation> fillingStations = getFillingStations();
    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    final List<FillingStation> bestFuelPrice = fillingStationService.getBestFuelPrice(46.970797, 28.727367, 100000, fuelType,
        LIMIT);

    final List<String> fillingStationsName = bestFuelPrice.stream()
        .map(FillingStation::getName)
        .collect(toList());
    assertThat(bestFuelPrice.size()).isEqualTo(2);
    assertThat(fillingStationsName).containsAll(asList(FILLING_STATION_WITH_BEST_FUEL_PRICE_1,
        FILLING_STATION_WITH_BEST_FUEL_PRICE_2));

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw InvalidRequestException on getAllFillingStations if found more than 10 filling stations")
  void shouldThrowInvalidRequestExceptionOnAllFillingStationsInRadius() {
    final int limit = 10;
    final List<FillingStation> fillingStations = getFillingStations();
    when(anreApi.getFillingStationsInfo()).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationService.getAllFillingStations(47.018334, 28.860920, 1500000, limit))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));

    verify(anreApi).getFillingStationsInfo();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on invalid fuelType")
  void shouldThrowEntityNotFoundExceptionOnInvalidFuelType() {
    assertThatThrownBy(() -> fillingStationService.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, "Invalid Fuel Type", LIMIT))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_INVALID_FUEL_TYPE);

    verifyNoInteractions(anreApi);
  }

  private List<FillingStation> getFillingStations() {
    final FillingStation fillingStation = new FillingStation(FILLING_STATION_NAME, 10.0, 10.0, 10.0, 46.976887, 28.881841);
    final FillingStation fillingStation1 = new FillingStation(FILLING_STATION_WITH_BEST_FUEL_PRICE_1, 5.0, 6.0, 7.0, 47.027295,
        28.850302);
    final FillingStation fillingStation2 = new FillingStation(FILLING_STATION_WITH_BEST_FUEL_PRICE_2, 5.0, 6.0, 7.0, 47.022135,
        28.825429);
    final FillingStation fillingStation3 = new FillingStation(FILLING_STATION_NAME, 12.0, 30.0, 102.0, 47.030981, 28.874094);
    final FillingStation fillingStation4 = new FillingStation(FILLING_STATION_NAME, null, 40.0, 103.0, 46.992269, 28.885449);
    final FillingStation fillingStation5 = new FillingStation(FILLING_STATION_NAME, 14.0, null, 104.0, 46.996093, 28.845470);
    final FillingStation fillingStation6 = new FillingStation(FILLING_STATION_NAME, 15.0, 60.0, null, 47.013886, 28.801868);
    final FillingStation fillingStation7 = new FillingStation(FILLING_STATION_NAME, 0D, 70.0, 106.0, 47.008970, 28.805302);
    final FillingStation fillingStation8 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 107.0, 47.021611, 28.904865);
    final FillingStation fillingStation9 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 10.0, 47.048286, 28.819721);
    final FillingStation fillingStation10 = new FillingStation(FILLING_STATION_NAME, 0D, 0D, 0D, 47.000308, 28.856800);
    final FillingStation fillingStation11 = new FillingStation(FILLING_STATION_IN_ANOTHER_LOCATION, 19.0, 100.0, 109.0, 46.975248,
        28.744190);
    return asList(fillingStation, fillingStation1, fillingStation2, fillingStation3, fillingStation4, fillingStation5,
        fillingStation6, fillingStation7, fillingStation8, fillingStation9, fillingStation10, fillingStation11);
  }
}
