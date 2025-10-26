package md.electric.api.facade;

import java.util.List;
import md.electric.api.rest.dto.ElectricStationDto;
import md.electric.api.rest.dto.PageDto;
import md.electric.api.rest.request.ElectricStationRequest;
import md.electric.api.rest.request.PageRequest;

public interface ElectricStationFacade {

  List<ElectricStationDto> getElectricStations();

  PageDto<ElectricStationDto> getElectricStations(ElectricStationRequest request, PageRequest pageRequest);
}