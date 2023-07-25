package md.bot.fuel.infrastructure.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.infrastructure.api.AnreApi;
import md.bot.fuel.infrastructure.exception.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.InvalidRequestException;
import org.springframework.stereotype.Service;

import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static md.bot.fuel.infrastructure.utils.DistanceCalculator.calculateMeters;
import static md.bot.fuel.infrastructure.utils.DistanceCalculator.isWithinRadius;

@Service
@RequiredArgsConstructor
public class FuelStationServiceImpl implements FuelStationService {

    private static final String ERROR_NO_FUEL_STATION_NEAR_YOU = "We can't find any fuel station near you. Try to extend search radius.";
    private static final String ERROR_FOUND_MORE_THAN_LIMIT = "We found more than %s fuel stations near you. Try to decrease search radius.";
    private static final String ERROR_NO_FUEL_IN_STOCK = "Fuel station near you do not have %s in stock. Try to extend search radius.";
    private static final String ERROR_INVALID_FUEL_TYPE = "Invalid fuel type.";

    private static final String PETROL = "Petrol";
    private static final String DIESEL = "Diesel";
    private static final String GAS = "Gas";
    private static final Double ZERO_PRICE = 0D;
    private static final double ZERO_PRICE_PRIMITIVE = 0D;

    private final AnreApi anreApi;

    @Override
    public List<FuelStation> getAllFuelStations(double userLatitude, double userLongitude, double radius, int limit) {
        final List<FuelStation> fuelStations = anreApi.getFuelStationsInfo().stream()
                .filter(s -> isWithinRadius(userLatitude, userLongitude, s.getLatitude(), s.getLongitude(), radius) &&
                        ((!isNull(s.getPetrol()) && s.getPetrol() > ZERO_PRICE_PRIMITIVE) ||
                                (!isNull(s.getGas()) && s.getGas() > ZERO_PRICE_PRIMITIVE) ||
                                (!isNull(s.getDiesel()) && s.getDiesel() > ZERO_PRICE_PRIMITIVE)))
                .collect(toList());

        checkLimit(fuelStations.size(), limit);
        if (fuelStations.isEmpty()) {
            throw new EntityNotFoundException(ERROR_NO_FUEL_STATION_NEAR_YOU);
        }
        return fuelStations;
    }

    @Override
    public FuelStation getNearestFuelStation(double userLatitude, double userLongitude, double radius) {
        return anreApi.getFuelStationsInfo().stream()
                .filter(s -> (!isNull(s.getPetrol()) && s.getPetrol() > ZERO_PRICE_PRIMITIVE) ||
                        (!isNull(s.getGas()) && s.getGas() > ZERO_PRICE_PRIMITIVE) ||
                        (!isNull(s.getDiesel()) && s.getDiesel() > ZERO_PRICE_PRIMITIVE))
                .min(comparing(s -> calculateMeters(userLatitude, userLongitude, s.getLatitude(), s.getLongitude())))
                .orElseThrow(() -> new EntityNotFoundException(ERROR_NO_FUEL_STATION_NEAR_YOU));
    }

    @Override
    public List<FuelStation> getBestFuelPrice(double userLatitude, double userLongitude, double radius,
                                              String fuelType, int limit) {
        final Function<FuelStation, Double> fuelStationFunction = getFuelType(fuelType);

        final List<FuelStation> filteredFuelStationsList = anreApi.getFuelStationsInfo().stream()
                .filter(s -> isWithinRadius(userLatitude, userLongitude, s.getLatitude(), s.getLongitude(), radius)
                        && !isNull(fuelStationFunction.apply(s))
                        && !Objects.equals(fuelStationFunction.apply(s), ZERO_PRICE))
                .collect(toList());

        final double minimalFuelPrice = filteredFuelStationsList.stream()
                .mapToDouble(fuelStationFunction::apply)
                .min()
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_NO_FUEL_IN_STOCK, fuelType.toLowerCase())));

        final List<FuelStation> fuelStations = filteredFuelStationsList.stream()
                .filter(station -> fuelStationFunction.apply(station).equals(minimalFuelPrice))
                .collect(toList());

        checkLimit(fuelStations.size(), limit);

        return fuelStations;
    }

    private Function<FuelStation, Double> getFuelType(String fuelType) {
        switch (fuelType) {
            case PETROL: {
                return FuelStation::getPetrol;
            }
            case DIESEL: {
                return FuelStation::getDiesel;
            }
            case GAS: {
                return FuelStation::getGas;
            }
        }
        throw new EntityNotFoundException(ERROR_INVALID_FUEL_TYPE);
    }

    private void checkLimit(int numberOfFuelStation, int limit) {
        if (numberOfFuelStation > limit) {
            throw new InvalidRequestException(String.format(ERROR_FOUND_MORE_THAN_LIMIT, limit));
        }
    }
}
