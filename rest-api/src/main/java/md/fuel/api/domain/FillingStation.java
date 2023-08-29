package md.fuel.api.domain;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;
import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.rest.request.SortingQuery;

public record FillingStation(
    String name,
    Double petrol,
    Double diesel,
    Double gas,
    double latitude,
    double longitude) implements Serializable {

  public static String TIMESTAMP;
  private static final Map<String, Comparator<FillingStation>> COMPARATORS;

  static {
    COMPARATORS = new HashMap<>();
    COMPARATORS.put("name", comparing(FillingStation::name));
    COMPARATORS.put("petrol", comparing(FillingStation::petrol, nullsLast(Double::compareTo)));
    COMPARATORS.put("diesel", comparing(FillingStation::diesel, nullsLast(Double::compareTo)));
    COMPARATORS.put("gas", comparing(FillingStation::gas, nullsLast(Double::compareTo)));

    //Stub: Because the distance comparator needs the user's position, a different way of creating the comparator is used.
    // This construction allows you to avoid an error in the method getComparator
    COMPARATORS.put("distance", (f1, f2) -> 0);
  }

  public static Comparator<FillingStation> getComparator(SortingQuery sortingQuery) {
    final Comparator<FillingStation> comparator = COMPARATORS.get(sortingQuery.sortOrderBy());

    if (isNull(comparator)) {
      throw new InvalidRequestException("Wrong sorting parameter: " + sortingQuery.sortOrderBy(), "INVALID_SORT_PARAM");
    }
    return sortingQuery.isAscending() ? comparator : comparator.reversed();
  }
}