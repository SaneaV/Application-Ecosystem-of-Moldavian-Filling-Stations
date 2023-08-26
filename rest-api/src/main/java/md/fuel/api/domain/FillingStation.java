package md.fuel.api.domain;

import java.io.Serializable;

public record FillingStation(
    String name,
    Double petrol,
    Double diesel,
    Double gas,
    double latitude,
    double longitude) implements Serializable {

  public static String timestamp;
}