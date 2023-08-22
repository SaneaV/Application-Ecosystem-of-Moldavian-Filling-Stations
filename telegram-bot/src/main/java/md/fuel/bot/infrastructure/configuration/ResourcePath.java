package md.fuel.bot.infrastructure.configuration;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ResourcePath {

  public static final String ALL_FILLING_STATIONS_PAGE_PATH = "all-filling-stations-page";
  public static final String NEAREST_PATH = "nearest";
  public static final String BEST_FUEL_PRICE_PAGE_PATH = "best-fuel-price-page";
  public static final String LAST_UPDATE_PATH = "last-update";
  public static final String FUEL_TYPE_PATH = "fuel-type";
}
