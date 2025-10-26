package md.electric.api.domain.criteria;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import md.electric.api.domain.ConnectorType;

@Getter
@RequiredArgsConstructor
public class ElectricStationCriteria {

  private final double latitude;
  private final double longitude;
  private final double radius;
  private final List<ConnectorType> connectors;
  private final List<SortingQuery> sorting;
  private final Integer pageLimit;
  private final Integer pageOffset;
}
