package md.fuel.api.domain.criteria;

import java.util.List;
import lombok.Getter;
import md.fuel.api.rest.request.SortingQuery;

@Getter
public class LimitFillingStationCriteria extends BaseFillingStationCriteria {

  private final int limitInRadius;
  private final List<SortingQuery> sorting;
  private final Integer pageLimit;
  private final Integer pageOffset;

  public LimitFillingStationCriteria(double latitude, double longitude, double radius, int limitInRadius,
      List<SortingQuery> sorting, Integer pageLimit, Integer pageOffset) {
    super(latitude, longitude, radius);
    this.limitInRadius = limitInRadius;
    this.sorting = sorting;
    this.pageLimit = pageLimit;
    this.pageOffset = pageOffset;
  }
}