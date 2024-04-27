package md.fuel.bot.telegram.converter;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import org.springframework.data.util.Pair;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class MessageConverter {

  private static final double ZERO_PRICE = 0.0D;
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";
  private static final String START_PART_MESSAGE = "⛽ Filling station - \"%s\"\n\n";
  private static final String LAST_PART_MESSAGE = "\n📊 Last price update: %s";
  private static final String FUEL_PART_MESSAGE = "%s %s: %s lei\n";

  public static String toMessage(FillingStation fillingStation) {
    log.debug("Convert data about filling station into the text");
    final StringBuilder message = new StringBuilder()
        .append(String.format(START_PART_MESSAGE, fillingStation.getName()));

    fillingStation.getPrices().forEach((fuelName, fuelPrice) -> {
      final Pair<String, Double> priceIndicator = getPriceIndicator(fuelPrice);
      final String priceMessage = String.format(FUEL_PART_MESSAGE, priceIndicator.getFirst(), fuelName,
          priceIndicator.getSecond());
      message.append(priceMessage);
    });

    message.append(String.format(LAST_PART_MESSAGE, FillingStation.timestamp));

    return message.toString();
  }

  public static String toMessage(FillingStation fillingStation, String fuelType) {
    log.debug("Convert data about filling station and fuel type = {} into the text", fuelType);
    final Pair<String, Double> priceIndicator = getSpecificFuelTypePriceIndicator(fillingStation, fuelType);
    return String.format(START_PART_MESSAGE, fillingStation.getName())
        + String.format(FUEL_PART_MESSAGE, priceIndicator.getFirst(), fuelType, priceIndicator.getSecond())
        + String.format(LAST_PART_MESSAGE, FillingStation.timestamp);
  }

  private static Pair<String, Double> getPriceIndicator(Double price) {
    return isNull(price) ? Pair.of(RED_CIRCLE, ZERO_PRICE) : Pair.of(GREEN_CIRCLE, price);
  }

  private static Pair<String, Double> getSpecificFuelTypePriceIndicator(FillingStation fillingStation, String fuelType) {
    return getPriceIndicator(fillingStation.getPrices().get(fuelType));
  }
}