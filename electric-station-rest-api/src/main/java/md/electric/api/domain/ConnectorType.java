package md.electric.api.domain;

public enum ConnectorType {
  CCS2("CCS2"),
  CCS1("CCS1"),
  CHADEMO("CHAdeMO"),
  J1772("J-1772"),
  TESLA_ROADSTER("Tesla (Roadster)"),
  TYPE_2("Type 2"),
  THREE_PHASE("Three Phase"),
  CARAVAN_MAINS("Caravan Mains Socket"),
  GB_T("GB/T"),
  GB_T_FAST("GB/T (Fast)"),
  WALL_EURO("Wall (Euro)"),
  THREE_PHASE_EU("Three Phase EU"),
  TYPE_3A("Type 3A"),
  TYPE_3("Type 3");

  private final String value;

  ConnectorType(String value) {
    this.value = value;
  }

  public static ConnectorType fromValue(String value) {
    for (ConnectorType type : ConnectorType.values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown connector type: " + value);
  }
}