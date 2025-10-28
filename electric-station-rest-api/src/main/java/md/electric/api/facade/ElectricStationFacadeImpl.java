package md.electric.api.facade;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.criteria.ElectricStationCriteria;
import md.electric.api.infrastructure.exception.model.EntityNotFoundException;
import md.electric.api.infrastructure.mapper.CriteriaMapper;
import md.electric.api.infrastructure.service.ElectricStationService;
import md.electric.api.rest.dto.ElectricStationDto;
import md.electric.api.rest.dto.PageDto;
import md.electric.api.rest.mapper.ElectricStationDtoMapper;
import md.electric.api.rest.request.ElectricStationRequest;
import md.electric.api.rest.request.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElectricStationFacadeImpl implements ElectricStationFacade {

  private static final String MOLDOVA_ZONE_DATE_TIME = "Europe/Chisinau";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final String ERROR_UPDATE_MESSAGE = "Data not updated yet";
  private static final String ERROR_UPDATE_REASON_CODE = "NO_TIMESTAMP";

  private final ElectricStationService service;
  private final ElectricStationDtoMapper dtoMapper;
  private final CriteriaMapper criteriaMapper;

  @Override
  public List<ElectricStationDto> getElectricStations() {
    return service.getElectricStations().stream()
        .map(dtoMapper::toDto)
        .toList();
  }

  @Override
  public PageDto<ElectricStationDto> getElectricStations(ElectricStationRequest request, PageRequest pageRequest) {
    final ElectricStationCriteria criteria = criteriaMapper.toEntity(request, pageRequest);
    final List<ElectricStation> electricStations = service.getElectricStations(criteria);

    final List<ElectricStation> filteredFillingStations = filterByOffsetAndLimit(electricStations, criteria.getPageLimit(),
        criteria.getPageOffset());

    return dtoMapper.toDto(filteredFillingStations, electricStations.size());
  }

  private List<ElectricStation> filterByOffsetAndLimit(List<ElectricStation> electricStations, Integer limit, Integer offset) {
    if (isNull(limit) || isNull(offset)) {
      return electricStations;
    }
    return electricStations.stream()
        .skip(offset)
        .limit(limit)
        .toList();
  }

  @Override
  public ZonedDateTime getLastUpdatedDateTime() {
    log.info("Fetching last data update");

    if (isNull(ElectricStation.TIMESTAMP)) {
      throw new EntityNotFoundException(ERROR_UPDATE_MESSAGE, ERROR_UPDATE_REASON_CODE);
    }
    return LocalDateTime.parse(ElectricStation.TIMESTAMP, FORMATTER).atZone(ZoneId.of(MOLDOVA_ZONE_DATE_TIME));
  }
}

