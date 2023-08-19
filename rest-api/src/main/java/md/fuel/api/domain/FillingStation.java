package md.fuel.api.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FillingStation implements Serializable {

  public static String timestamp;

  private final String name;
  private final Double petrol;
  private final Double diesel;
  private final Double gas;
  private final double latitude;
  private final double longitude;
}