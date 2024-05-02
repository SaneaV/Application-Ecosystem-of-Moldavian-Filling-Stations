package md.fuel.api.facade;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelType;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.mapper.CriteriaMapper;
import md.fuel.api.infrastructure.service.FillingStationService;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FillingStationDtoMapper;
import md.fuel.api.rest.dto.FuelPriceDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingStationFacadeImpl implements FillingStationFacade {

  private static final String ERROR_LIMIT_MESSAGE = "The limit should be greater than 0";
  private static final String ERROR_UPDATE_MESSAGE = "There is no data to retrieve. Made any request to update the timestamp.";
  private static final String ERROR_LIMIT_REASON_CODE = "INVALID_LIMIT";
  private static final String ERROR_UPDATE_REASON_CODE = "NO_DATA";
  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  private static final String MOLDOVA_ZONE_DATE_TIME = "Europe/Chisinau";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  private final FillingStationService fillingStationService;
  private final FillingStationDtoMapper fillingStationDtoMapper;
  private final CriteriaMapper criteriaMapper;

  @Override
  public PageDto<FillingStationDto> getPageOfFillingStations(LimitFillingStationRequest limitFillingStationRequest,
      PageRequest pageRequest) {
    log.info("Fetching page of filling stations");

    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(limitFillingStationRequest, pageRequest);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getAllFillingStations(criteria);
    final List<FillingStation> filteredFillingStations = filterByOffsetAndLimit(fillingStations, criteria.getPageLimit(),
        criteria.getPageOffset());

    return fillingStationDtoMapper.toDto(filteredFillingStations, fillingStations.size());
  }

  @Override
  public List<FillingStationDto> getAllFillingStations(LimitFillingStationRequest request) {
    log.info("Fetching list of filling stations");

    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(request);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getAllFillingStations(criteria);

    return fillingStationDtoMapper.toDto(fillingStations);
  }

  @Override
  public FillingStationDto getNearestFillingStation(BaseFillingStationRequest request) {
    log.info("Fetching data about nearest filling stations");

    final BaseFillingStationCriteria criteria = criteriaMapper.toEntity(request);
    final FillingStation fillingStation = fillingStationService.getNearestFillingStation(criteria);
    return fillingStationDtoMapper.toDto(fillingStation);
  }

  @Override
  public List<FillingStationDto> getBestFuelPrice(LimitFillingStationRequest request, String fuelType) {
    log.info("Fetching list of filling stations with best {} price", fuelType);

    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(request);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getBestFuelPrice(criteria, fuelType);

    return fillingStationDtoMapper.toDto(fillingStations);
  }

  @Override
  public PageDto<FillingStationDto> getPageOfBestFuelPrices(LimitFillingStationRequest limitFillingStationRequest,
      PageRequest pageRequest, String fuelType) {
    log.info("Fetching page of filling stations with best {} price", fuelType.toLowerCase());

    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(limitFillingStationRequest, pageRequest);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getBestFuelPrice(criteria, fuelType);
    final List<FillingStation> filteredFillingStations = filterByOffsetAndLimit(fillingStations, criteria.getPageLimit(),
        criteria.getPageOffset());

    return fillingStationDtoMapper.toDto(filteredFillingStations, fillingStations.size());
  }

  @Override
  public ZonedDateTime getLastUpdateTimestamp() {
    log.info("Fetching last data update");

    if (isNull(FillingStation.TIMESTAMP)) {
      throw new EntityNotFoundException(ERROR_UPDATE_MESSAGE, ERROR_UPDATE_REASON_CODE);
    }
    return LocalDateTime.parse(FillingStation.TIMESTAMP, FORMATTER).atZone(ZoneId.of(MOLDOVA_ZONE_DATE_TIME));
  }

  @Override
  public List<String> getAvailableFuelTypes() {
    log.info("Fetching all available fuel types");

    return Arrays.stream(FuelType.values())
        .map(FuelType::getDescription)
        .toList();
  }

  @Override
  public FuelPriceDto getAnrePrices() {
    log.info("Fetching info about official ANRE prices");

    return fillingStationDtoMapper.toDto(fillingStationService.getAnrePrices());
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

  private void checkLimit(int limit) {
    if (limit <= 0) {
      log.error("User specified limit is less or equal to 0");

      throw new InvalidRequestException(ERROR_LIMIT_MESSAGE, ERROR_LIMIT_REASON_CODE);
    }
  }
}