package md.fuel.bot.telegram.converter;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.infrastructure.service.TranslatorService;
import org.springframework.data.util.Pair;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class MessageConverter {

  private static final double ZERO_PRICE = 0.0D;
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";
  private static final String START_PART_MESSAGE = "response.name.message";
  private static final String LAST_PART_MESSAGE = "response.price-update.message";
  private static final String FUEL_PART_MESSAGE = "response.price.message";

  public static String toMessage(FillingStation fillingStation, TranslatorService translatorService, String language) {
    log.debug("Convert data about filling station into the text");
    final String translatedStartPartMessage = translate(translatorService, START_PART_MESSAGE, language);
    final String translatedFuelPartMessage = translate(translatorService, FUEL_PART_MESSAGE, language);
    final String translatedLastPartMessage = translate(translatorService, LAST_PART_MESSAGE, language);

    final StringBuilder message = new StringBuilder()
        .append(String.format(translatedStartPartMessage, fillingStation.getName()));

    fillingStation.getPrices().forEach((fuelName, fuelPrice) -> {
      final String keyForTranslation = FuelTypeNormalizer.normalize(fuelName);
      final String translatedFuelName = translate(translatorService, keyForTranslation, language);
      final Pair<String, Double> priceIndicator = getPriceIndicator(fuelPrice);
      final String priceMessage = String.format(translatedFuelPartMessage, priceIndicator.getFirst(), translatedFuelName,
          priceIndicator.getSecond());
      message.append(priceMessage);
    });

    message.append(String.format(translatedLastPartMessage, FillingStation.timestamp));

    return message.toString();
  }

  public static String toMessage(FillingStation fillingStation, String fuelType, TranslatorService translatorService,
      String language) {
    log.debug("Convert data about filling station and fuel type = {} into the text", fuelType);
    final String translatedStartPartMessage = translate(translatorService, START_PART_MESSAGE, language);
    final String translatedFuelPartMessage = translate(translatorService, FUEL_PART_MESSAGE, language);
    final String translatedLastPartMessage = translate(translatorService, LAST_PART_MESSAGE, language);
    final String englishFuelType = FuelTypeNormalizer.toEnglish(translatorService,  fuelType);

    final Pair<String, Double> priceIndicator = getSpecificFuelTypePriceIndicator(fillingStation, englishFuelType);
    return String.format(translatedStartPartMessage, fillingStation.getName())
        + String.format(translatedFuelPartMessage, priceIndicator.getFirst(), fuelType, priceIndicator.getSecond())
        + String.format(translatedLastPartMessage, FillingStation.timestamp);
  }

  private static Pair<String, Double> getPriceIndicator(Double price) {
    return isNull(price) ? Pair.of(RED_CIRCLE, ZERO_PRICE) : Pair.of(GREEN_CIRCLE, price);
  }

  private static Pair<String, Double> getSpecificFuelTypePriceIndicator(FillingStation fillingStation, String fuelType) {
    return getPriceIndicator(fillingStation.getPrices().get(fuelType));
  }

  private static String translate(TranslatorService translatorService, String message, String language) {
    return translatorService.translate(language, message);
  }
}