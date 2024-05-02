package md.fuel.api.infrastructure.service;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;

public interface FillingStationService {

  List<FillingStation> getAllFillingStations(LimitFillingStationCriteria criteria);

  FillingStation getNearestFillingStation(BaseFillingStationCriteria criteria);

  List<FillingStation> getBestFuelPrice(LimitFillingStationCriteria criteria, String fuelType);

  FuelPrice getAnrePrices();
}