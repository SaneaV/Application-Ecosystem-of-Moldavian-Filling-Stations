package md.fuel.api.facade;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillingStationFacadeImpl implements FillingStationFacade {

  private static final String ERROR_FOUND_MORE_THAN_LIMIT =
      "More than %s gas stations were found. This is more than your specified limit. Decrease the search radius.";
  private static final String ERROR_EXCEED_LIMIT_REASON_CODE = "EXCEED_LIMIT";
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
  public List<FillingStationDto> getAllFillingStations(LimitFillingStationRequest request) {
    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(request);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getAllFillingStations(criteria);
    checkLimit(fillingStations.size(), criteria.getLimitInRadius());

    return fillingStationDtoMapper.toDto(fillingStations);
  }

  @Override
  public FillingStationDto getNearestFillingStation(BaseFillingStationRequest request) {
    final BaseFillingStationCriteria criteria = criteriaMapper.toEntity(request);
    final FillingStation fillingStation = fillingStationService.getNearestFillingStation(criteria);
    return fillingStationDtoMapper.toDto(fillingStation);
  }

  @Override
  public List<FillingStationDto> getBestFuelPrice(LimitFillingStationRequest request, String fuelType) {
    final LimitFillingStationCriteria criteria = criteriaMapper.toEntity(request);

    checkLimit(criteria.getLimitInRadius());
    final List<FillingStation> fillingStations = fillingStationService.getBestFuelPrice(criteria, fuelType);
    checkLimit(fillingStations.size(), criteria.getLimitInRadius());

    return fillingStationDtoMapper.toDto(fillingStations);
  }

  @Override
  public ZonedDateTime getLastUpdateTimestamp() {
    if (isNull(FillingStation.TIMESTAMP)) {
      throw new EntityNotFoundException(ERROR_UPDATE_MESSAGE, ERROR_UPDATE_REASON_CODE);
    }
    return LocalDateTime.parse(FillingStation.TIMESTAMP, FORMATTER).atZone(ZoneId.of(MOLDOVA_ZONE_DATE_TIME));
  }

  @Override
  public List<String> getAvailableFuelTypes() {
    return Arrays.stream(FuelType.values())
        .map(FuelType::getDescription)
        .toList();
  }

  private void checkLimit(int limit) {
    if (limit <= 0) {
      throw new InvalidRequestException(ERROR_LIMIT_MESSAGE, ERROR_LIMIT_REASON_CODE);
    }
  }

  private void checkLimit(int numberOfFillingStations, int limit) {
    if (numberOfFillingStations > limit) {
      throw new InvalidRequestException(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit), ERROR_EXCEED_LIMIT_REASON_CODE);
    }
  }
}
