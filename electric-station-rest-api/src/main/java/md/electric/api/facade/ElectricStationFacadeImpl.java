package md.electric.api.facade;

import static java.util.Objects.isNull;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.criteria.ElectricStationCriteria;
import md.electric.api.infrastructure.mapper.CriteriaMapper;
import md.electric.api.infrastructure.service.ElectricStationService;
import md.electric.api.rest.dto.ElectricStationDto;
import md.electric.api.rest.dto.PageDto;
import md.electric.api.rest.mapper.ElectricStationDtoMapper;
import md.electric.api.rest.request.ElectricStationRequest;
import md.electric.api.rest.request.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElectricStationFacadeImpl implements ElectricStationFacade {

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
}