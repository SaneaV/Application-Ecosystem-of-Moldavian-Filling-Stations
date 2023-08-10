package md.bot.fuel.facade;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.facade.dto.FuelStationDtoMapper;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import md.bot.fuel.infrastructure.service.FuelStationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FuelStationFacadeImplTest {

  private static final String name = "Fuel Station";
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int VALID_LIMIT = 50;
  private static final int INVALID_LIMIT = 0;
  private static final double MOCK_PRICE = 0;
  private static final String FUEL_TYPE = "Petrol";
  private static final String ERROR_MESSAGE = "The limit should be greater than 0";

  private final FuelStationFacade fuelStationFacade;
  private final FuelStationService fuelStationService;
  private final FuelStationDtoMapper fuelStationDtoMapper;

  public FuelStationFacadeImplTest() {
    this.fuelStationService = mock(FuelStationService.class);
    this.fuelStationDtoMapper = mock(FuelStationDtoMapper.class);
    this.fuelStationFacade = new FuelStationFacadeImpl(fuelStationService, fuelStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getAllFuelStations")
  void shouldThrowInvalidRequestExceptionOnGetAllFuelStations() {
    assertThatThrownBy(() -> fuelStationFacade.getAllFuelStations(LATITUDE, LONGITUDE, RADIUS, INVALID_LIMIT))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_MESSAGE);

    verifyNoInteractions(fuelStationService);
    verifyNoInteractions(fuelStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getBestFuelPrice")
  void shouldThrowInvalidRequestExceptionOnGetBestFuelPrice() {
    assertThatThrownBy(() -> fuelStationFacade.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, INVALID_LIMIT))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_MESSAGE);

    verifyNoInteractions(fuelStationService);
    verifyNoInteractions(fuelStationDtoMapper);
  }

  @Test
  @DisplayName("Should return list of all fuel stations")
  void shouldReturnListOfAllFuelStation() {
    final FuelStation fuelStation = new FuelStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FuelStation fuelStation2 = new FuelStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FuelStation> fuelStations = new ArrayList<>(asList(fuelStation, fuelStation2));
    final FuelStationDto fuelStationDto = new FuelStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fuelStationService.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenReturn(fuelStations);
    when(fuelStationDtoMapper.toDto(any())).thenReturn(fuelStationDto);

    final List<FuelStationDto> allFuelStations = fuelStationFacade.getAllFuelStations(LATITUDE, LONGITUDE, RADIUS, VALID_LIMIT);

    verify(fuelStationService).getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt());
    verify(fuelStationDtoMapper, times(2)).toDto(any());

    assertThat(allFuelStations).isEqualTo(allFuelStations);
  }

  @Test
  @DisplayName("Should return nearest fuel station")
  void shouldReturnNearestFuelStation() {
    final FuelStation fuelStation = new FuelStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FuelStationDto fuelStationDto = new FuelStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fuelStationService.getNearestFuelStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(fuelStation);
    when(fuelStationDtoMapper.toDto(any())).thenReturn(fuelStationDto);

    final FuelStationDto nearestFuelStation = fuelStationFacade.getNearestFuelStation(LATITUDE, LONGITUDE, RADIUS);

    verify(fuelStationService).getNearestFuelStation(anyDouble(), anyDouble(), anyDouble());
    verify(fuelStationDtoMapper).toDto(any());

    assertThat(nearestFuelStation).isEqualTo(fuelStationDto);
  }

  @Test
  @DisplayName("Should return list of best fuel price stations")
  void shouldReturnListOfBestFuelPriceFuelStations() {
    final FuelStation fuelStation = new FuelStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FuelStation fuelStation2 = new FuelStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FuelStation> fuelStations = new ArrayList<>(asList(fuelStation, fuelStation2));
    final FuelStationDto fuelStationDto = new FuelStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fuelStationService.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), any(), anyInt())).thenReturn(fuelStations);
    when(fuelStationDtoMapper.toDto(any())).thenReturn(fuelStationDto);

    final List<FuelStationDto> bestFuelPriceStation = fuelStationFacade.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE,
        VALID_LIMIT);

    verify(fuelStationService).getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), any(), anyInt());
    verify(fuelStationDtoMapper, times(2)).toDto(any());

    assertThat(bestFuelPriceStation).isEqualTo(bestFuelPriceStation);
  }
}
