package md.fuel.bot.telegram.converter;

import java.util.Map;

public class ThirdPartyTranslationConverter {

  private static final String ERROR_DURING_MESSAGE_SENDING = "error.sending-message-error.message";
  private static final String ERROR_NO_PETROL_STATION = "error.no-petrol-station.message";
  private static final String ERROR_NO_DIESEL_STATION = "error.no-diesel-station.message";
  private static final String ERROR_NO_GAS_STATION = "error.no-gas-station.message";
  private static final String ERROR_NO_FILLING_STATION = "error.no-filling-station.message";

  private static final Map<String, String> THIRD_PARTY_MESSAGE_TO_KEY = Map.of(
      "An error occurred during message sending.", ERROR_DURING_MESSAGE_SENDING,
      "Filling stations within the specified radius do not have petrol in stock. Increase the search radius.",
      ERROR_NO_PETROL_STATION,
      "Filling stations within the specified radius do not have diesel in stock. Increase the search radius.",
      ERROR_NO_DIESEL_STATION,
      "Filling stations within the specified radius do not have gas in stock. Increase the search radius.", ERROR_NO_GAS_STATION,
      "No filling stations were found in the specified radius. Change the search point or increase the radius.", ERROR_NO_FILLING_STATION

  );

  public static String getKeyForThirdPartyMessage(String message) {
    return THIRD_PARTY_MESSAGE_TO_KEY.get(message);
  }
}