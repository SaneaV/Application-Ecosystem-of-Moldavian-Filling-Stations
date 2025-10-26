package md.electric.api.infrastructure.service;

import static java.util.stream.Collectors.toList;
import static md.electric.api.infrastructure.utils.DistanceCalculator.isWithinRadius;
import static md.electric.api.infrastructure.utils.MultiComparator.sort;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.Location;
import md.electric.api.domain.criteria.ElectricStationCriteria;
import md.electric.api.domain.criteria.SortingQuery;
import md.electric.api.infrastructure.client.plugshare.PlugShareClient;
import md.electric.api.infrastructure.configuration.ApiConfiguration;
import md.electric.api.infrastructure.exception.model.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElectricStationServiceImpl implements ElectricStationService {

  private static final String ERROR_NO_FILLING_STATION_NEAR_YOU =
      "No filling stations were found in the specified radius. Change the search point or increase the radius.";
  private static final String ERROR_NOT_FOUND_REASON_CODE = "NOT_FOUND";

  private final PlugShareClient plugShareClient;
  private final ApiConfiguration apiConfig;

  @Override
  public List<ElectricStation> getElectricStations() {
    return plugShareClient.fetchStations(apiConfig.getDefaultLatitude(), apiConfig.getDefaultLongitude());
  }

  @Override
  public List<ElectricStation> getElectricStations(ElectricStationCriteria criteria) {
    final double latitude = criteria.getLatitude();
    final double longitude = criteria.getLongitude();
    final double radius = criteria.getRadius();

    final List<ElectricStation> electricStations =
        plugShareClient.fetchStations(apiConfig.getDefaultLatitude(), apiConfig.getDefaultLongitude()).stream()
            .filter(s -> {
              final Location location = s.getLocation();
              return isWithinRadius(latitude, longitude, location.getLatitude(), location.getLongitude(), radius);
            })
            .collect(toList());

    if (electricStations.isEmpty()) {
      throw new EntityNotFoundException(ERROR_NO_FILLING_STATION_NEAR_YOU, ERROR_NOT_FOUND_REASON_CODE);
    }

    sort(electricStations, getComparators(criteria.getSorting(), latitude, longitude));
    return electricStations;
  }

  private List<Comparator<ElectricStation>> getComparators(List<SortingQuery> sortingQuery, double latitude, double longitude) {

    return sortingQuery.stream()
        .map(query -> ElectricStation.getComparator(query, latitude, longitude))
        .collect(toList());
  }
}
