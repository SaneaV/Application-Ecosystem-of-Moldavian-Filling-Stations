package md.fuel.bot.facade;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationFacadeTest {

  private static final String PETROL = "Petrol";
  private static final String DIESEL = "Diesel";
  private static final String GAS = "Gas";
  private static final Double DEFAULT_DOUBLE = 10.0;
  private static final int DEFAULT_INT = 0;
  private static final FillingStation FILLING_STATION_1 = new FillingStation("name1",
      Map.of(PETROL, DEFAULT_DOUBLE, DIESEL, DEFAULT_DOUBLE, GAS, DEFAULT_DOUBLE), DEFAULT_DOUBLE, DEFAULT_DOUBLE);
  private static final FillingStation FILLING_STATION_2 = new FillingStation("name2",
      Map.of(PETROL, DEFAULT_DOUBLE, DIESEL, DEFAULT_DOUBLE, GAS, DEFAULT_DOUBLE), DEFAULT_DOUBLE, DEFAULT_DOUBLE);
  private static final List<FillingStation> FILLING_STATIONS_LIST = asList(FILLING_STATION_1, FILLING_STATION_2);
  private static final Page<FillingStation> FILLING_STATIONS_PAGE = new Page<>(2, FILLING_STATIONS_LIST);
  private static final String TIMESTAMP = ZonedDateTime.now().toString();

  private final FillingStationFacade facade;
  private final FillingStationRepository repository;

  public FillingStationFacadeTest() {
    this.repository = mock(FillingStationRepository.class);
    this.facade = new FillingStationFacadeImpl(this.repository);
  }

  @Test
  @DisplayName("Should return list of all filling stations")
  void shouldReturnListOfAllFillingStation() {
    when(repository.getUpdateTimestamp()).thenReturn(TIMESTAMP);
    when(repository.getAllFillingStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(FILLING_STATIONS_PAGE);

    final FillingStation result = facade.getAllFillingStations(DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_INT);

    assertThat(result).isEqualTo(FILLING_STATIONS_LIST.get(DEFAULT_INT));
    assertThat(FillingStation.timestamp).isEqualTo(TIMESTAMP);
    verify(repository).getAllFillingStation(anyDouble(), anyDouble(), anyDouble());
    verify(repository).getUpdateTimestamp();
    FillingStation.timestamp = null;
  }

  @Test
  @DisplayName("Should return nearest filling station")
  void shouldReturnNearestFillingStation() {
    when(repository.getUpdateTimestamp()).thenReturn(TIMESTAMP);
    when(repository.getNearestFillingStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(FILLING_STATION_1);

    final FillingStation result = facade.getNearestFillingStation(DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE);

    assertThat(result).isEqualTo(FILLING_STATION_1);
    assertThat(FillingStation.timestamp).isEqualTo(TIMESTAMP);
    verify(repository).getNearestFillingStation(anyDouble(), anyDouble(), anyDouble());
    verify(repository).getUpdateTimestamp();
    FillingStation.timestamp = null;
  }

  @Test
  @DisplayName("Should return list of best fuel price stations")
  void shouldReturnListOfBestFuelPriceFillingStations() {
    when(repository.getUpdateTimestamp()).thenReturn(TIMESTAMP);
    when(repository.getBestFuelPriceStation(anyDouble(), anyDouble(), anyDouble(), anyString())).thenReturn(
        FILLING_STATIONS_PAGE);

    final FillingStation result = facade.getBestFuelPrice(DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, EMPTY, DEFAULT_INT);

    assertThat(result).isEqualTo(FILLING_STATIONS_LIST.get(DEFAULT_INT));
    assertThat(FillingStation.timestamp).isEqualTo(TIMESTAMP);
    verify(repository).getBestFuelPriceStation(anyDouble(), anyDouble(), anyDouble(), anyString());
    verify(repository).getUpdateTimestamp();
    FillingStation.timestamp = null;
  }

  @Test
  @DisplayName("Should return list of fuel types")
  void shouldReturnListOfFuelTypes() {
    final List<String> expected = asList(PETROL, DIESEL, GAS);
    final FuelType fuelType = new FuelType(expected);

    when(repository.getSupportedFuelTypes()).thenReturn(fuelType);

    final FuelType result = facade.getSupportedFuelTypes();

    assertThat(result).isEqualTo(fuelType);
    verify(repository).getSupportedFuelTypes();
  }
}