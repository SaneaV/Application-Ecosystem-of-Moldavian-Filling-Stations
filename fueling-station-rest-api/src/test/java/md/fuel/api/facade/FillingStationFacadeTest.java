//package md.fuel.api.facade;
//
//import static java.util.Arrays.asList;
//import static java.util.Collections.emptyList;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import md.fuel.api.domain.FillingStation;
//import md.fuel.api.domain.FuelPrice;
//import md.fuel.api.domain.FuelType;
//import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
//import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
//import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
//import md.fuel.api.infrastructure.mapper.CriteriaMapper;
//import md.fuel.api.infrastructure.service.FillingStationService;
//import md.fuel.api.rest.dto.FillingStationDto;
//import md.fuel.api.rest.dto.FillingStationDtoMapper;
//import md.fuel.api.rest.dto.FuelPriceDto;
//import md.fuel.api.rest.dto.PageDto;
//import md.fuel.api.rest.request.BaseFillingStationRequest;
//import md.fuel.api.rest.request.LimitFillingStationRequest;
//import md.fuel.api.rest.request.PageRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class FillingStationFacadeTest {
//
//  private static final String NAME = "Filling Station";
//  private static final double LATITUDE = 10;
//  private static final double LONGITUDE = 20;
//  private static final double RADIUS = 30;
//  private static final int VALID_LIMIT = 50;
//  private static final int INVALID_LIMIT = 0;
//  private static final double MOCK_PRICE = 0;
//  private static final String FUEL_TYPE = "Petrol";
//  private static final String ERROR_INVALID_LIMIT = "The limit should be greater than 0";
//
//  private final FillingStationFacade fillingStationFacade;
//  private final FillingStationService fillingStationService;
//  private final FillingStationDtoMapper fillingStationDtoMapper;
//  private final CriteriaMapper criteriaMapper;
//
//  public FillingStationFacadeTest() {
//    this.fillingStationService = mock(FillingStationService.class);
//    this.fillingStationDtoMapper = mock(FillingStationDtoMapper.class);
//    this.criteriaMapper = mock(CriteriaMapper.class);
//    this.fillingStationFacade = new FillingStationFacadeImpl(fillingStationService, fillingStationDtoMapper, criteriaMapper);
//  }
//
//  @Test
//  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getAllFillingStations")
//  void shouldThrowInvalidRequestExceptionOnGetAllFillingStationsOnLessLimit() {
//    final LimitFillingStationRequest request = buildRequest(INVALID_LIMIT);
//
//    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(INVALID_LIMIT));
//
//    assertThatThrownBy(() -> fillingStationFacade.getAllFillingStations(request))
//        .isInstanceOf(InvalidRequestException.class)
//        .hasMessage(ERROR_INVALID_LIMIT);
//
//    verify(criteriaMapper).toEntity(any());
//    verifyNoInteractions(fillingStationService);
//    verifyNoInteractions(fillingStationDtoMapper);
//  }
//
//  @Test
//  @DisplayName("Should throw InvalidRequestException if limit is less than or equal to zero on getBestFuelPrice")
//  void shouldThrowInvalidRequestExceptionOnGetBestFuelPriceOnLessLimit() {
//    final LimitFillingStationRequest request = buildRequest(INVALID_LIMIT);
//
//    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(INVALID_LIMIT));
//
//    assertThatThrownBy(() -> fillingStationFacade.getBestFuelPrice(request, FUEL_TYPE))
//        .isInstanceOf(InvalidRequestException.class)
//        .hasMessage(ERROR_INVALID_LIMIT);
//
//    verify(criteriaMapper).toEntity(any());
//    verifyNoInteractions(fillingStationService);
//    verifyNoInteractions(fillingStationDtoMapper);
//  }
//
//  @Test
//  @DisplayName("Should return list of all filling stations")
//  void shouldReturnListOfAllFillingStation() {
//    final FillingStation fillingStation = new FillingStation(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
//    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
//    final FillingStationDto fillingStationDto = new FillingStationDto(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
//        LONGITUDE);
//    final LimitFillingStationRequest request = buildRequest(VALID_LIMIT);
//
//    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(VALID_LIMIT));
//    when(fillingStationService.getAllFillingStations(any())).thenReturn(fillingStations);
//    when(fillingStationDtoMapper.toDto(anyList())).thenReturn(asList(fillingStationDto, fillingStationDto));
//
//    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(request);
//
//    verify(criteriaMapper).toEntity(any());
//    verify(fillingStationService).getAllFillingStations(any());
//    verify(fillingStationDtoMapper).toDto(anyList());
//
//    assertThat(allFillingStations).containsExactly(fillingStationDto, fillingStationDto);
//  }
//
//  @Test
//  @DisplayName("Should return nearest filling station")
//  void shouldReturnNearestFillingStation() {
//    final FillingStation fillingStation = new FillingStation(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
//    final FillingStationDto fillingStationDto = new FillingStationDto(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
//        LONGITUDE);
//
//    when(fillingStationService.getNearestFillingStation(any())).thenReturn(fillingStation);
//    when(fillingStationDtoMapper.toDto(any(FillingStation.class))).thenReturn(fillingStationDto);
//
//    final BaseFillingStationRequest request = buildRequest();
//
//    final FillingStationDto nearestFillingStation = fillingStationFacade.getNearestFillingStation(request);
//
//    verify(fillingStationService).getNearestFillingStation(any());
//    verify(fillingStationDtoMapper).toDto(any(FillingStation.class));
//
//    assertThat(nearestFillingStation).isEqualTo(fillingStationDto);
//  }
//
//  @Test
//  @DisplayName("Should return list of best fuel price stations")
//  void shouldReturnListOfBestFuelPriceFillingStations() {
//    final FillingStation fillingStation = new FillingStation(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
//    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
//    final FillingStationDto fillingStationDto = new FillingStationDto(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
//        LONGITUDE);
//    final LimitFillingStationRequest request = buildRequest(VALID_LIMIT);
//
//    when(criteriaMapper.toEntity(any())).thenReturn(buildCriteria(VALID_LIMIT));
//    when(fillingStationService.getBestFuelPrice(any(), anyString())).thenReturn(fillingStations);
//    when(fillingStationDtoMapper.toDto(anyList())).thenReturn(asList(fillingStationDto, fillingStationDto));
//
//    final List<FillingStationDto> bestFuelPriceStation = fillingStationFacade.getBestFuelPrice(request, FUEL_TYPE);
//
//    verify(criteriaMapper).toEntity(any());
//    verify(fillingStationService).getBestFuelPrice(any(), anyString());
//    verify(fillingStationDtoMapper).toDto(anyList());
//
//    assertThat(bestFuelPriceStation).containsExactly(fillingStationDto, fillingStationDto);
//  }
//
//  @Test
//  @DisplayName("Should return timestamp of filling station data update")
//  void shouldReturnTimestampOfFillingStationDataUpdate() {
//    final String dateTimeFormat = "dd.MM.yyyy HH:mm";
//    final String moldovaZoneDateTime = "Europe/Chisinau";
//    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
//
//    FillingStation.TIMESTAMP = ZonedDateTime.now().format(formatter);
//
//    final ZonedDateTime expected = LocalDateTime.parse(FillingStation.TIMESTAMP, formatter)
//        .atZone(ZoneId.of(moldovaZoneDateTime));
//    final ZonedDateTime result = fillingStationFacade.getLastUpdateTimestamp();
//
//    assertThat(result).isEqualTo(expected);
//  }
//
//  @Test
//  @DisplayName("Should throw exception if no filling station was fetched")
//  void shouldThrowExceptionIfNoFillingStationWasFetched() {
//    assertThatThrownBy(fillingStationFacade::getLastUpdateTimestamp)
//        .isInstanceOf(EntityNotFoundException.class)
//        .hasMessage("There is no data to retrieve. Made any request to update the timestamp.");
//  }
//
//  @Test
//  @DisplayName("Should return list of fuel types")
//  void shouldReturnListOfFuelTypes() {
//    final List<String> expected = Arrays.stream(FuelType.values())
//        .map(FuelType::getDescription)
//        .toList();
//
//    final List<String> result = fillingStationFacade.getAvailableFuelTypes();
//
//    assertThat(result).containsExactlyElementsOf(expected);
//  }
//
//  @Test
//  @DisplayName("Should return official ANRE prices")
//  void shouldReturnOfficialAnrePrices() {
//    final double petrolPrice = 10.0;
//    final double dieselPrice = 10.0;
//    final String date = LocalDate.now().toString();
//    final FuelPrice fuelPrice = new FuelPrice(petrolPrice, dieselPrice, date);
//    final FuelPriceDto fuelPriceDto = new FuelPriceDto(date, petrolPrice, dieselPrice);
//
//    when(fillingStationService.getAnrePrices()).thenReturn(fuelPrice);
//    when(fillingStationDtoMapper.toDto(any(FuelPrice.class))).thenReturn(fuelPriceDto);
//
//    final FuelPriceDto result = fillingStationFacade.getAnrePrices();
//
//    verify(fillingStationService).getAnrePrices();
//    verify(fillingStationDtoMapper).toDto(any(FuelPrice.class));
//
//    assertThat(result).isEqualTo(fuelPriceDto);
//  }
//
//  @Test
//  @DisplayName("Should get page of filling stations")
//  void shouldGetPageOfFillingStations() {
//    final FillingStation fillingStation = new FillingStation(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
//    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
//    final FillingStationDto fillingStationDto = new FillingStationDto(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
//        LONGITUDE);
//    final LimitFillingStationRequest limitFillingStationRequest = buildRequest(VALID_LIMIT);
//    final PageRequest pageRequest = new PageRequest();
//    pageRequest.setOffset(0);
//    pageRequest.setLimit(20);
//    final int numberOfStations = 2;
//    final PageDto<FillingStationDto> expected = new PageDto<>(numberOfStations, List.of(fillingStationDto, fillingStationDto));
//
//    when(criteriaMapper.toEntity(any(), any())).thenReturn(buildCriteria(10));
//    when(fillingStationService.getAllFillingStations(any())).thenReturn(fillingStations);
//    when(fillingStationDtoMapper.toDto(anyList(), anyInt())).thenReturn(expected);
//
//    final PageDto<FillingStationDto> page = fillingStationFacade.getPageOfFillingStations(limitFillingStationRequest,
//        pageRequest);
//
//    verify(criteriaMapper).toEntity(any(), any());
//    verify(fillingStationService).getAllFillingStations(any());
//    verify(fillingStationDtoMapper).toDto(anyList(), anyInt());
//
//    assertThat(page.getTotalResults()).isEqualTo(numberOfStations);
//    assertThat(page.getItems()).containsExactly(fillingStationDto, fillingStationDto);
//  }
//
//  @Test
//  @DisplayName("Should get page of best fuel price stations")
//  void shouldGetPageOfBestFuelPriceStations() {
//    final FillingStation fillingStation = new FillingStation(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE, LONGITUDE);
//    final List<FillingStation> fillingStations = new ArrayList<>(asList(fillingStation, fillingStation));
//    final FillingStationDto fillingStationDto = new FillingStationDto(NAME, MOCK_PRICE, MOCK_PRICE, MOCK_PRICE, LATITUDE,
//        LONGITUDE);
//    final LimitFillingStationRequest request = buildRequest(VALID_LIMIT);
//    final PageRequest pageRequest = new PageRequest();
//    pageRequest.setOffset(0);
//    pageRequest.setLimit(20);
//    final int numberOfStations = 2;
//    final PageDto<FillingStationDto> expected = new PageDto<>(numberOfStations, List.of(fillingStationDto, fillingStationDto));
//
//    when(criteriaMapper.toEntity(any(), any())).thenReturn(buildCriteria(10));
//    when(fillingStationService.getBestFuelPrice(any(), anyString())).thenReturn(fillingStations);
//    when(fillingStationDtoMapper.toDto(anyList(), anyInt())).thenReturn(expected);
//
//    final PageDto<FillingStationDto> result = fillingStationFacade.getPageOfBestFuelPrices(request, pageRequest, FUEL_TYPE);
//
//    verify(criteriaMapper).toEntity(any(), any());
//    verify(fillingStationService).getBestFuelPrice(any(), anyString());
//    verify(fillingStationDtoMapper).toDto(anyList(), anyInt());
//
//    assertThat(result.getTotalResults()).isEqualTo(numberOfStations);
//    assertThat(result.getItems()).containsExactly(fillingStationDto, fillingStationDto);
//  }
//
//  private LimitFillingStationCriteria buildCriteria(int limitInRadius) {
//    return new LimitFillingStationCriteria(LATITUDE, LONGITUDE, RADIUS, limitInRadius, emptyList(), 1000, 0);
//  }
//
//  private LimitFillingStationRequest buildRequest(int limitInRadius) {
//    final LimitFillingStationRequest request = new LimitFillingStationRequest();
//    request.setLatitude(LATITUDE);
//    request.setLongitude(LONGITUDE);
//    request.setRadius(RADIUS);
//    request.setLimit_in_radius(limitInRadius);
//    return request;
//  }
//
//  private BaseFillingStationRequest buildRequest() {
//    final BaseFillingStationRequest request = new BaseFillingStationRequest();
//    request.setLatitude(LATITUDE);
//    request.setLongitude(LONGITUDE);
//    request.setRadius(RADIUS);
//    return request;
//  }
//}
