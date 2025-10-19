package md.electric.api.domain;

import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChargingStation implements Serializable {

  private final String name;
  private Location location;
  private final int stationCount;
  private final Set<ConnectorType> connectorTypes;
}