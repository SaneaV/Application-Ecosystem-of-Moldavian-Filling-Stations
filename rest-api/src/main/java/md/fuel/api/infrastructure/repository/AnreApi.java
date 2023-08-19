package md.fuel.api.infrastructure.repository;

import java.util.List;
import md.fuel.api.domain.FillingStation;

public interface AnreApi {

  List<FillingStation> getFillingStationsInfo();
}
