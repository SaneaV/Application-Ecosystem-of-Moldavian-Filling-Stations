package md.bot.fuel.telegram.converter;

import lombok.NoArgsConstructor;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.infrastructure.exception.EntityNotFoundException;
import org.springframework.data.util.Pair;

import static lombok.AccessLevel.NONE;

@NoArgsConstructor(access = NONE)
public class MessageConverter {

    private static final double ZERO_PRICE = 0.0D;
    private static final String GREEN_CIRCLE = "\uD83D\uDFE2";
    private static final String RED_CIRCLE = "\uD83D\uDD34";
    private static final String FUEL_STATION_MESSAGE = "⛽ Fuel station - \"%s\"\n\n" +
            "%s Petrol: %s lei\n" +
            "%s Diesel: %s lei\n" +
            "%s Gas : %s lei\n\n" +
            "\uD83D\uDCCA Last price update: %s";
    private static final String SPECIFIC_FUEL_STATION_MESSAGE = "⛽ Fuel station - \"%s\"\n\n" +
            "%s %s: %s lei\n\n" +
            "\uD83D\uDCCA Last price update: %s";
    private static final String PETROL = "Petrol";
    private static final String DIESEL = "Diesel";
    private static final String GAS = "Gas";
    private static final String ERROR_NO_FUEL_TYPE_EXIST = "Can't find specified fuel type";

    public static String toMessage(FuelStationDto fuelStation) {
        final Pair<String, Double> petrolPrice = getPrice(fuelStation.getPetrol());
        final Pair<String, Double> dieselPrice = getPrice(fuelStation.getDiesel());
        final Pair<String, Double> gasPrice = getPrice(fuelStation.getGas());
        return String.format(FUEL_STATION_MESSAGE, fuelStation.getName(),
                petrolPrice.getFirst(), petrolPrice.getSecond(),
                dieselPrice.getFirst(), dieselPrice.getSecond(),
                gasPrice.getFirst(), gasPrice.getSecond(),
                FuelStationDto.timestamp);
    }

    public static String toMessage(FuelStationDto fuelStation, String fuelType) {
        final Pair<String, Double> fuelPrice = getPrice(fuelStation, fuelType);
        return String.format(SPECIFIC_FUEL_STATION_MESSAGE, fuelStation.getName(), fuelPrice.getFirst(), fuelType,
                fuelPrice.getSecond(), FuelStationDto.timestamp);
    }

    private static Pair<String, Double> getPrice(Double price) {
        return price == null ? Pair.of(RED_CIRCLE, ZERO_PRICE) : Pair.of(GREEN_CIRCLE, price);
    }

    private static Pair<String, Double> getPrice(FuelStationDto fuelStation, String fuelType) {
        switch (fuelType) {
            case PETROL: {
                return getPrice(fuelStation.getPetrol());
            }
            case DIESEL: {
                return getPrice(fuelStation.getDiesel());
            }
            case GAS: {
                return getPrice(fuelStation.getGas());
            }
        }
        throw new EntityNotFoundException(ERROR_NO_FUEL_TYPE_EXIST);
    }
}
