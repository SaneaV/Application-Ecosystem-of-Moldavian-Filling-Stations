package md.fuel.api.facade;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.service.FillingStationService;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FillingStationDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationFacadeImplTest {

  private static final String name = "Filling Station";
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int VALID_LIMIT = 50;
  private static final int INVALID_LIMIT = 0;
  private static final double MOCK_PRICE = 0;
  private static final String FUEL_TYPE = "Petrol";
  private static final String ERROR_MESSAGE = "The limit should be greater than 0";

  private final FillingStationFacade fillingStationFacade;
  private final FillingStationService fillingStationService;
  private final FillingStationDtoMapper fillingStationDtoMapper;

  public FillingStationFacadeImplTest() {
    this.fillingStationService = mock(FillingStationService.class);
    this.fillingStationDtoMapper = mock(FillingStationDtoMapper.class);
    this.fillingStationFacade = new FillingStationFacadeImpl(fillingStationService, fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getAllFillingStations")
  void shouldThrowInvalidRequestExceptionOnGetAllFillingStations() {
    assertThatThrownBy(() -> fillingStationFacade.getAllFillingStations(LATITUDE, LONGITUDE, RADIUS, INVALID_LIMIT))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_MESSAGE);

    verifyNoInteractions(fillingStationService);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getBestFuelPrice")
  void shouldThrowInvalidRequestExceptionOnGetBestFuelPrice() {
    assertThatThrownBy(() -> fillingStationFacade.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE, INVALID_LIMIT))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_MESSAGE);

    verifyNoInteractions(fillingStationService);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should return list of all filling stations")
  void shouldReturnListOfAllFillingStation() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FillingStation fillingStation2 = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation2));
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fillingStationService.getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenReturn(fillingStations);
    when(fillingStationDtoMapper.toDto(any())).thenReturn(fillingStationDto);

    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(LATITUDE, LONGITUDE, RADIUS, VALID_LIMIT);

    verify(fillingStationService).getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt());
    verify(fillingStationDtoMapper, times(2)).toDto(any());

    assertThat(allFillingStations).containsExactly(fillingStationDto, fillingStationDto);
  }

  @Test
  @DisplayName("Should return nearest filling station")
  void shouldReturnNearestFillingStation() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fillingStationService.getNearestFillingStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(fillingStation);
    when(fillingStationDtoMapper.toDto(any())).thenReturn(fillingStationDto);

    final FillingStationDto nearestFillingStation = fillingStationFacade.getNearestFillingStation(LATITUDE, LONGITUDE, RADIUS);

    verify(fillingStationService).getNearestFillingStation(anyDouble(), anyDouble(), anyDouble());
    verify(fillingStationDtoMapper).toDto(any());

    assertThat(nearestFillingStation).isEqualTo(fillingStationDto);
  }

  @Test
  @DisplayName("Should return list of best fuel price stations")
  void shouldReturnListOfBestFuelPriceFillingStations() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FillingStation fillingStation2 = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation2));
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);

    when(fillingStationService.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), any(), anyInt())).thenReturn(fillingStations);
    when(fillingStationDtoMapper.toDto(any())).thenReturn(fillingStationDto);

    final List<FillingStationDto> bestFuelPriceStation = fillingStationFacade.getBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE,
        VALID_LIMIT);

    verify(fillingStationService).getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), any(), anyInt());
    verify(fillingStationDtoMapper, times(2)).toDto(any());

    assertThat(bestFuelPriceStation).containsExactly(fillingStationDto, fillingStationDto);
  }
}
