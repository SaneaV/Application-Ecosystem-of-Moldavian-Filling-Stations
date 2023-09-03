package md.fuel.api.infrastructure.mapper;

import static java.util.Arrays.asList;
import static md.fuel.api.rest.request.SortOrder.ASC;
import static md.fuel.api.rest.request.SortOrder.DESC;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.SortingQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CriteriaMapperTest {

  private static final double LATITUDE = 10D;
  private static final double LONGITUDE = 20D;
  private static final double RADIUS = 30D;
  private static final int LIMIT_IN_RADIUS = 10;
  private static final String DISTANCE = "distance";
  private static final List<String> SORTING = asList("+distance", "-distance", DISTANCE);

  private final CriteriaMapper mapper;

  CriteriaMapperTest() {
    this.mapper = new CriteriaMapperImpl();
  }

  @Test
  @DisplayName("Should map BaseFillingStationRequest to BaseFillingStationCriteria")
  void shouldMapBaseFillingStationRequestToBaseFillingStationCriteria() {
    final BaseFillingStationRequest request = new BaseFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);

    final BaseFillingStationCriteria criteria = mapper.toEntity(request);

    assertThat(criteria).usingRecursiveComparison().isEqualTo(request);
  }

  @Test
  @DisplayName("Should map BaseFillingStationRequest to null BaseFillingStationCriteria")
  void shouldMapBaseFillingStationRequestToNullBaseFillingStationCriteria() {
    final BaseFillingStationCriteria criteria = mapper.toEntity((BaseFillingStationRequest) null);

    assertThat(criteria).isNull();
  }

  @Test
  @DisplayName("Should map LimitFillingStationRequest to LimitFillingStationCriteria")
  void shouldMapLimitFillingStationRequestToLimitFillingStationCriteria() {
    final LimitFillingStationRequest request = new LimitFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    request.setLimit_in_radius(LIMIT_IN_RADIUS);
    request.setSorting(SORTING);

    final List<SortingQuery> sorting = new ArrayList<>(asList(new SortingQuery(DISTANCE, ASC),
        new SortingQuery(DISTANCE, DESC), new SortingQuery(DISTANCE, ASC)));

    final LimitFillingStationCriteria criteria = mapper.toEntity(request);

    assertThat(criteria).usingRecursiveComparison().ignoringFields("sorting")
        .isEqualTo(request);
    assertThat(criteria.getSorting()).usingRecursiveComparison().isEqualTo(sorting);
  }

  @Test
  @DisplayName("Should map LimitFillingStationRequest to null LimitFillingStationCriteria")
  void shouldMapLimitFillingStationRequestToNullLimitFillingStationCriteria() {
    final LimitFillingStationCriteria criteria = mapper.toEntity(null);

    assertThat(criteria).isNull();
  }
}
