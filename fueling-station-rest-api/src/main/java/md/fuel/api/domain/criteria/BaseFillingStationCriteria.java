package md.fuel.api.domain.criteria;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseFillingStationCriteria {

  private final double latitude;
  private final double longitude;
  private final double radius;
}