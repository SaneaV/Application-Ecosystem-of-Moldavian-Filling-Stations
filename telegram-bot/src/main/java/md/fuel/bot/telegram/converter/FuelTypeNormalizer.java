package md.fuel.bot.telegram.converter;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.service.TranslatorService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FuelTypeNormalizer {

  private static final String PETROL_KEY = "fuel.petrol.message";
  private static final String DIESEL_KEY = "fuel.diesel.message";
  private static final String GAS_KEY = "fuel.gas.message";

  private static final Map<String, String> SUPPORTED = Map.of(
      "Petrol", PETROL_KEY,
      "Бензин", PETROL_KEY,
      "Benzină", PETROL_KEY,
      "Diesel", DIESEL_KEY,
      "Дизель", DIESEL_KEY,
      "Motorină", DIESEL_KEY,
      "Gas", GAS_KEY,
      "Газ", GAS_KEY,
      "Gaz", GAS_KEY
  );

  public static String normalize(String input) {
    return SUPPORTED.getOrDefault(input, input);
  }

  public static String toEnglish(TranslatorService translatorService, String input) {
    final String key = normalize(input);
    return translatorService.translate("en", key);
  }
}
