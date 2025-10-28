package md.electric.api.domain;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;
import static java.util.Objects.isNull;
import static md.electric.api.infrastructure.utils.DistanceCalculator.calculateMeters;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import md.electric.api.domain.criteria.SortingQuery;
import md.electric.api.infrastructure.exception.model.InvalidRequestException;

@Getter
@Setter
@RequiredArgsConstructor
public class ElectricStation implements Serializable {

  private static final Map<String, Comparator<ElectricStation>> COMPARATORS;
  public static String TIMESTAMP;

  private final String name;
  private Location location;
  private final int stationCount;
  private final Set<ConnectorType> connectorTypes;

  static {
    COMPARATORS = new HashMap<>();
    COMPARATORS.put("name", comparing(ElectricStation::getName));
  }

  public static Comparator<ElectricStation> getComparator(SortingQuery sortingQuery, double latitude, double longitude) {
    final String sortOrderBy = sortingQuery.getSortOrderBy();

    updateDistanceComparator(latitude, longitude);
    final Comparator<ElectricStation> comparator = COMPARATORS.get(sortOrderBy);

    if (isNull(comparator)) {
      throw new InvalidRequestException("Wrong sorting parameter: " + sortOrderBy, "INVALID_SORT_PARAM");
    }
    return sortingQuery.isAscending() ? comparator : comparator.reversed();
  }

  private static void updateDistanceComparator(double latitude, double longitude) {
    COMPARATORS.put("distance", comparingDouble(s -> {
      final Location loc = s.getLocation();
      return calculateMeters(latitude, longitude, loc.getLatitude(), loc.getLongitude());
    }));
  }
}