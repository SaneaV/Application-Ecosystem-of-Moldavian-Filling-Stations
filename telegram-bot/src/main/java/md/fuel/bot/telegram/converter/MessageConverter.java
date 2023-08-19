package md.fuel.bot.telegram.converter;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import org.springframework.data.util.Pair;

@NoArgsConstructor(access = PRIVATE)
public class MessageConverter {

  private static final double ZERO_PRICE = 0.0D;
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";
  private static final String FILLING_STATION_MESSAGE = "⛽ Filling station - \"%s\"\n\n"
      + "%s Petrol: %s lei\n"
      + "%s Diesel: %s lei\n"
      + "%s Gas : %s lei\n\n"
      + "📊 Last price update: %s";
  private static final String SPECIFIC_FILLING_STATION_MESSAGE = "⛽ Filling station - \"%s\"\n\n"
      + "%s %s: %s lei\n\n"
      + "📊 Last price update: %s";
  private static final String PETROL = "Petrol";
  private static final String DIESEL = "Diesel";
  private static final String GAS = "Gas";
  private static final String ERROR_NO_FUEL_TYPE_EXIST = "Can't find specified fuel type";

  public static String toMessage(FillingStation fillingStation) {
    final Pair<String, Double> petrolPrice = getPrice(fillingStation.getPetrol());
    final Pair<String, Double> dieselPrice = getPrice(fillingStation.getDiesel());
    final Pair<String, Double> gasPrice = getPrice(fillingStation.getGas());
    return String.format(FILLING_STATION_MESSAGE, fillingStation.getName(),
        petrolPrice.getFirst(), petrolPrice.getSecond(),
        dieselPrice.getFirst(), dieselPrice.getSecond(),
        gasPrice.getFirst(), gasPrice.getSecond(),
        FillingStation.timestamp);
  }

  public static String toMessage(FillingStation fillingStation, String fuelType) {
    final Pair<String, Double> fuelPrice = getPrice(fillingStation, fuelType);
    return String.format(SPECIFIC_FILLING_STATION_MESSAGE, fillingStation.getName(), fuelPrice.getFirst(), fuelType,
        fuelPrice.getSecond(), FillingStation.timestamp);
  }

  private static Pair<String, Double> getPrice(Double price) {
    return price == null ? Pair.of(RED_CIRCLE, ZERO_PRICE) : Pair.of(GREEN_CIRCLE, price);
  }

  //TODO: Price and fuel should be like a map
  private static Pair<String, Double> getPrice(FillingStation fillingStation, String fuelType) {
    switch (fuelType) {
      case PETROL: {
        return getPrice(fillingStation.getPetrol());
      }
      case DIESEL: {
        return getPrice(fillingStation.getDiesel());
      }
      case GAS: {
        return getPrice(fillingStation.getGas());
      }
      default: {
        throw new EntityNotFoundException(ERROR_NO_FUEL_TYPE_EXIST);
      }
    }
  }
}
