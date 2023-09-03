package md.fuel.api.facade;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelType;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.mapper.CriteriaMapper;
import md.fuel.api.infrastructure.service.FillingStationService;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FillingStationDtoMapper;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationFacadeTest {

  private static final String name = "Filling Station";
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int VALID_LIMIT = 50;
  private static final int INVALID_LIMIT = 0;
  private static final double MOCK_PRICE = 0;
  private static final String FUEL_TYPE = "Petrol";
  private static final String ERROR_INVALID_LIMIT = "The limit should be greater than 0";
  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "More than %s gas stations were found. This is more than your specified limit. Decrease the search radius.";

  private final FillingStationFacade fillingStationFacade;
  private final FillingStationService fillingStationService;
  private final FillingStationDtoMapper fillingStationDtoMapper;
  private final CriteriaMapper criteriaMapper;

  public FillingStationFacadeTest() {
    this.fillingStationService = mock(FillingStationService.class);
    this.fillingStationDtoMapper = mock(FillingStationDtoMapper.class);
    this.criteriaMapper = mock(CriteriaMapper.class);
    this.fillingStationFacade = new FillingStationFacadeImpl(fillingStationService, fillingStationDtoMapper, criteriaMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getAllFillingStations")
  void shouldThrowInvalidRequestExceptionOnGetAllFillingStationsOnLessLimit() {
    final LimitFillingStationRequest request = buildRequest(INVALID_LIMIT);

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(INVALID_LIMIT));

    assertThatThrownBy(() -> fillingStationFacade.getAllFillingStations(request))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_INVALID_LIMIT);

    verify(criteriaMapper).toEntity(any());
    verifyNoInteractions(fillingStationService);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if number of results greater than limit on getAllFillingStations")
  void shouldThrowInvalidRequestExceptionOnGetAllFillingStationsOnGreaterLimit() {
    final int limit = 1;
    final LimitFillingStationRequest request = buildRequest(limit);
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(limit));
    when(fillingStationService.getAllFillingStations(any())).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationFacade.getAllFillingStations(request))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));

    verify(criteriaMapper).toEntity(any());
    when(fillingStationService.getAllFillingStations(any())).thenReturn(fillingStations);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getBestFuelPrice")
  void shouldThrowInvalidRequestExceptionOnGetBestFuelPriceOnLessLimit() {
    final LimitFillingStationRequest request = buildRequest(INVALID_LIMIT);

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(INVALID_LIMIT));

    assertThatThrownBy(() -> fillingStationFacade.getBestFuelPrice(request, FUEL_TYPE))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(ERROR_INVALID_LIMIT);

    verify(criteriaMapper).toEntity(any());
    verifyNoInteractions(fillingStationService);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should throw InvalidRequestException if number of results greater than limit on getBestFuelPrice")
  void shouldThrowInvalidRequestExceptionOnGetBestFuelPricesOnGreaterLimit() {
    final int limit = 1;
    final LimitFillingStationRequest request = buildRequest(limit);
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(limit));
    when(fillingStationService.getBestFuelPrice(any(), anyString())).thenReturn(fillingStations);

    assertThatThrownBy(() -> fillingStationFacade.getBestFuelPrice(request, FUEL_TYPE))
        .isInstanceOf(InvalidRequestException.class)
        .hasMessage(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));

    verify(criteriaMapper).toEntity(any());
    when(fillingStationService.getBestFuelPrice(any(), anyString())).thenReturn(fillingStations);
    verifyNoInteractions(fillingStationDtoMapper);
  }

  @Test
  @DisplayName("Should return list of all filling stations")
  void shouldReturnListOfAllFillingStation() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
        LONGITUDE);
    final LimitFillingStationRequest request = buildRequest(VALID_LIMIT);

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(VALID_LIMIT));
    when(fillingStationService.getAllFillingStations(any())).thenReturn(fillingStations);
    when(fillingStationDtoMapper.toDto(anyList())).thenReturn(asList(fillingStationDto, fillingStationDto));

    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(request);

    verify(criteriaMapper).toEntity(any());
    verify(fillingStationService).getAllFillingStations(any());
    verify(fillingStationDtoMapper).toDto(anyList());

    assertThat(allFillingStations).containsExactly(fillingStationDto, fillingStationDto);
  }

  @Test
  @DisplayName("Should return nearest filling station")
  void shouldReturnNearestFillingStation() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
        LONGITUDE);

    when(fillingStationService.getNearestFillingStation(any())).thenReturn(fillingStation);
    when(fillingStationDtoMapper.toDto(any(FillingStation.class))).thenReturn(fillingStationDto);

    final BaseFillingStationRequest request = buildRequest();

    final FillingStationDto nearestFillingStation = fillingStationFacade.getNearestFillingStation(request);

    verify(fillingStationService).getNearestFillingStation(any());
    verify(fillingStationDtoMapper).toDto(any(FillingStation.class));

    assertThat(nearestFillingStation).isEqualTo(fillingStationDto);
  }

  @Test
  @DisplayName("Should return list of best fuel price stations")
  void shouldReturnListOfBestFuelPriceFillingStations() {
    final FillingStation fillingStation = new FillingStation(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
    final FillingStationDto fillingStationDto = new FillingStationDto(name, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
        LONGITUDE);
    final LimitFillingStationRequest request = buildRequest(VALID_LIMIT);

    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(VALID_LIMIT));
    when(fillingStationService.getBestFuelPrice(any(), anyString())).thenReturn(fillingStations);
    when(fillingStationDtoMapper.toDto(anyList())).thenReturn(asList(fillingStationDto, fillingStationDto));

    final List<FillingStationDto> bestFuelPriceStation = fillingStationFacade.getBestFuelPrice(request, FUEL_TYPE);

    verify(criteriaMapper).toEntity(any());
    verify(fillingStationService).getBestFuelPrice(any(), anyString());
    verify(fillingStationDtoMapper).toDto(anyList());

    assertThat(bestFuelPriceStation).containsExactly(fillingStationDto, fillingStationDto);
  }

  @Test
  @DisplayName("Should return timestamp of filling station data update")
  void shouldReturnTimestampOfFillingStationDataUpdate() {
    final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    final String MOLDOVA_ZONE_DATE_TIME = "Europe/Chisinau";
    final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    FillingStation.TIMESTAMP = ZonedDateTime.now().format(FORMATTER);

    final ZonedDateTime expected = LocalDateTime.parse(FillingStation.TIMESTAMP, FORMATTER)
        .atZone(ZoneId.of(MOLDOVA_ZONE_DATE_TIME));
    final ZonedDateTime result = fillingStationFacade.getLastUpdateTimestamp();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should throw exception if no filling station was fetched")
  void shouldThrowExceptionIfNoFillingStationWasFetched() {
    assertThatThrownBy(fillingStationFacade::getLastUpdateTimestamp)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("There is no data to retrieve. Made any request to update the timestamp.");
  }

  @Test
  @DisplayName("Should return list of fuel types")
  void shouldReturnListOfFuelTypes() {
    final List<String> expected = Arrays.stream(FuelType.values())
        .map(FuelType::getDescription)
        .toList();

    final List<String> result = fillingStationFacade.getAvailableFuelTypes();

    assertThat(result).containsExactlyElementsOf(expected);
  }

  private LimitFillingStationRequest buildRequest(int limitInRadius) {
    final LimitFillingStationRequest request = new LimitFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    request.setLimit_in_radius(limitInRadius);
    return request;
  }

  private LimitFillingStationCriteria buildCriteria(int limitInRadius) {
    return new LimitFillingStationCriteria(LATITUDE, LONGITUDE, RADIUS, limitInRadius, emptyList());
  }

  private BaseFillingStationRequest buildRequest() {
    final BaseFillingStationRequest request = new BaseFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    return request;
  }
}
