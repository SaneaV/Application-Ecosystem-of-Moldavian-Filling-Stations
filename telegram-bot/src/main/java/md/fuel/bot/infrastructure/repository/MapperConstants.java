package md.fuel.bot.infrastructure.repository;

import static lombok.AccessLevel.PRIVATE;

import java.time.format.DateTimeFormatter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MapperConstants {

  public static final String EXCEPTION_MAPPING_MESSAGE = "Page mapping execution";
  public static final String EXCEPTION_METHOD_MESSAGE = "Method by signature not found.";
  public static final String LATITUDE = "latitude";
  public static final String LONGITUDE = "longitude";
  public static final String EXCEPTION_PRICE_MAPPING_MESSAGE = "Can't convert input price to map.";

  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}
