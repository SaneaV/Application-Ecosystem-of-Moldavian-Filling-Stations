package md.fuel.api.facade;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.infrastructure.service.FillingStationService;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.FillingStationDtoMapper;
import org.springframework.stereotype.Component;

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

  @Override
  public List<FillingStationDto> getAllFillingStations(double latitude, double longitude, double radius, int limit) {
    checkLimit(limit);
    final List<FillingStation> fillingStations = fillingStationService.getAllFillingStations(latitude, longitude, radius, limit);

    return fillingStations.stream()
        .map(fillingStationDtoMapper::toDto)
        .collect(toList());
  }

  @Override
  public FillingStationDto getNearestFillingStation(double latitude, double longitude, double radius) {
    final FillingStation fillingStation = fillingStationService.getNearestFillingStation(latitude, longitude, radius);
    return fillingStationDtoMapper.toDto(fillingStation);
  }

  @Override
  public List<FillingStationDto> getBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int limit) {
    checkLimit(limit);
    final List<FillingStation> fillingStations = fillingStationService.getBestFuelPrice(latitude, longitude, radius, fuelType,
        limit);

    return fillingStations.stream()
        .map(fillingStationDtoMapper::toDto)
        .collect(toList());
  }

  @Override
  public ZonedDateTime getLastUpdateTimestamp() {
    if (isNull(FillingStation.timestamp)) {
      throw new EntityNotFoundException(ERROR_UPDATE_MESSAGE, ERROR_UPDATE_REASON_CODE);
    }
    return LocalDateTime.parse(FillingStation.timestamp, FORMATTER).atZone(ZoneId.of(MOLDOVA_ZONE_DATE_TIME));
  }

  private void checkLimit(int limit) {
    if (limit <= 0) {
      throw new InvalidRequestException(ERROR_LIMIT_MESSAGE, ERROR_LIMIT_REASON_CODE);
    }
  }
}
