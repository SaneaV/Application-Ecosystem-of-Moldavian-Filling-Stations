package md.electric.api.infrastructure.service;

import java.util.List;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.criteria.ElectricStationCriteria;

public interface ElectricStationService {

  List<ElectricStation> getElectricStations();

  List<ElectricStation> getElectricStations(ElectricStationCriteria criteria);
}
