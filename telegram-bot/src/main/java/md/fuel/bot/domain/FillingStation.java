package md.fuel.bot.domain;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FillingStation implements Serializable {

  public static String timestamp;

  private final String name;
  private final Map<String, Double> prices;
  private final double latitude;
  private final double longitude;
}