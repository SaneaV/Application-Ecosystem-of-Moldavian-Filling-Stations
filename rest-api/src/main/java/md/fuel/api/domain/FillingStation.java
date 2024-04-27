package md.fuel.api.domain;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.nullsLast;
import static java.util.Objects.isNull;
import static md.fuel.api.infrastructure.utils.DistanceCalculator.calculateMeters;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.rest.request.SortingQuery;

@Slf4j
@Getter
@RequiredArgsConstructor
public class FillingStation implements Serializable {

  private static final Map<String, Comparator<FillingStation>> COMPARATORS;
  public static String TIMESTAMP;

  static {
    COMPARATORS = new HashMap<>();
    COMPARATORS.put("name", comparing(FillingStation::getName));
    COMPARATORS.put("petrol", comparing(FillingStation::getPetrol, nullsLast(Double::compareTo)));
    COMPARATORS.put("diesel", comparing(FillingStation::getDiesel, nullsLast(Double::compareTo)));
    COMPARATORS.put("gas", comparing(FillingStation::getGas, nullsLast(Double::compareTo)));
  }

  private final String name;
  private final Double petrol;
  private final Double diesel;
  private final Double gas;
  private final double latitude;
  private final double longitude;

  public static Comparator<FillingStation> getComparator(SortingQuery sortingQuery, double latitude, double longitude) {
    final String sortOrderBy = sortingQuery.getSortOrderBy();
    log.info("Get filling station comparator for {} in {} order", sortOrderBy, sortingQuery.getSortOrder());

    updateDistanceComparator(latitude, longitude);
    final Comparator<FillingStation> comparator = COMPARATORS.get(sortOrderBy);

    if (isNull(comparator)) {
      log.error("User specified wrong sorting parameter: {}", sortOrderBy);

      throw new InvalidRequestException("Wrong sorting parameter: " + sortOrderBy, "INVALID_SORT_PARAM");
    }
    return sortingQuery.isAscending() ? comparator : comparator.reversed();
  }

  private static void updateDistanceComparator(double latitude, double longitude) {
    log.info("Update distance comparator with user latitude: {} and longitude: {}", latitude, longitude);
    COMPARATORS.put("distance", comparingDouble(s -> calculateMeters(latitude, longitude, s.getLatitude(), s.getLongitude())));
  }
}