package md.fuel.bot.domain;

import java.io.Serializable;
import java.util.Map;

public record FillingStation(
    String name,
    Map<String, Double> prices,
    double latitude,
    double longitude) implements Serializable {

  public static String timestamp;

}