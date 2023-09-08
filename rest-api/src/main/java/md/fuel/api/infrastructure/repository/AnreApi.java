package md.fuel.api.infrastructure.repository;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;

public interface AnreApi {

  List<FillingStation> getFillingStationsInfo();

  FuelPrice getAnrePrices();
}
