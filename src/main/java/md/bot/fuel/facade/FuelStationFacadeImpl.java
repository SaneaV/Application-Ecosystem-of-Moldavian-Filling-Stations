package md.bot.fuel.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.FuelStation;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.facade.dto.FuelStationDtoMapper;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import md.bot.fuel.infrastructure.service.FuelStationService;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class FuelStationFacadeImpl implements FuelStationFacade {

    private static final String ERROR_MESSAGE = "The limit should be greater than 0";
    private static final String ERROR_REASON_CODE = "INVALID_LIMIT";

    private final FuelStationService fuelStationService;
    private final FuelStationDtoMapper fuelStationDtoMapper;

    @Override
    public List<FuelStationDto> getAllFuelStations(double userLatitude, double userLongitude, double radius, int limit) {
        checkLimit(limit);
        final List<FuelStation> fuelStations = fuelStationService.getAllFuelStations(userLatitude, userLongitude, radius, limit);

        return fuelStations.stream()
                .map(fuelStationDtoMapper::toDto)
                .collect(toList());
    }

    @Override
    public FuelStationDto getNearestFuelStation(double userLatitude, double userLongitude, double radius) {
        final FuelStation fuelStation = fuelStationService.getNearestFuelStation(userLatitude, userLongitude, radius);
        return fuelStationDtoMapper.toDto(fuelStation);
    }

    @Override
    public List<FuelStationDto> getBestFuelPrice(double userLatitude, double userLongitude, double radius, String fuelType,
                                                 int limit) {
        checkLimit(limit);
        final List<FuelStation> fuelStations = fuelStationService.getBestFuelPrice(userLatitude, userLongitude, radius, fuelType,
                limit);

        return fuelStations.stream()
                .map(fuelStationDtoMapper::toDto)
                .collect(toList());
    }

    private void checkLimit(int limit) {
        if (limit <= 0) {
            throw new InvalidRequestException(ERROR_MESSAGE, ERROR_REASON_CODE);
        }
    }
}
